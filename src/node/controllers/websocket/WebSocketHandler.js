'use strict';
const ClientMessages = require('./ClientMessages');
const ServerMessages = require('./ServerMessages');
const Models = require('../../models');
const ws = require('ws');
const ActiveTriviaGame = require('./ActiveTriviaGame');
const log4js = require('log4js');
let logger = log4js.getLogger();
logger.level = process.env.LOG_LEVEL || 'info';

/**
 * WebSocketHandler
 * Class for running the WebSocket server that handles real-time communication.
 */
class WebSocketHandler {
	/**
	 * constructor
	 * Starts the WebSocket server
	 * @param {*} port: Port that the server listens on.
	 * @param {*} validator: Function for validating connections; it should
	 * 		accept a token and return the corresponding user.
	 */
	constructor(port, validator) {
		this.validate = validator;
		this.wss = new ws.Server({
			port,
			verifyClient: this.verifyClient.bind(this)
		});

		// bind is used to keep the class (this) in the scope of the connectHandler
		this.wss.on('connection', this.connectHandler.bind(this));
		this.activeTrivia = null;
		this.interval = setInterval(this.ping.bind(this), 300000);
	}

	/**
	 * connectHandler
	 * Event handler for new client connections.
	 * @param {*} client: The client object for the new connection.
	 * @param {*} info: Additional information for the connection, contains
	 * 		the validated user object.
	 */
	connectHandler(client, info) {
		logger.debug('WS Connection');
		client.user = info.user;

		// base message handler needs to be defined inside connect handler to keep client in scope
		client.on('message', (data) => {
			let clientMessage;
			logger.debug(data);
			try {
				clientMessage = new ClientMessages.WSClientMessage(data);
			} catch (err) {
				this.sendError(`Error parsing websocket message: ${err}`, client);
				return;
			}

			if (clientMessage.messageType === ClientMessages.WSClientMessage.MESSAGE_TYPES.ClientServerChatMessage) {
				this.processChatMessage(clientMessage.payload, client);
			} else if (clientMessage.messageType === ClientMessages.WSClientMessage.MESSAGE_TYPES.HostServerMessage) {
				this.processTriviaHostMessage(clientMessage.payload, client);
			} else if (clientMessage.messageType === ClientMessages.WSClientMessage.MESSAGE_TYPES.PlayerServerMessage) {
				this.processPlayerMessage(clientMessage.payload, client);
			} else {
				this.sendError('Message type not handled', client);
			}
		});

		client.on('close', () => {
			logger.debug(`User: ${client.user.userName} disconnected`);
		});

		// Send chat history to client
		this.sendAllMessages(client);

		this.sendGameInfo(client);
		// Setup for tracking if client connection is broken.
		client.isAlive = true;
		client.on('pong', this.heartbeat);
	}

	/**
	 * ping
	 * Function for sending pings to clients and detecting if the connection is
	 * broken. If the client's 'isAlive' is false it is broken and connection is
	 * terminated. It is then set to false and a ping is sent.
	 */
	ping() {
		this.wss.clients.forEach((client) => {
			if (client.isAlive === false) {
				return client.terminate();
			}
			client.isAlive = false;
			client.ping(() => {});
		})
	}

	/**
	 * heartbeat
	 * Event handler for checking broken connections connections. When a client
	 * responds to a ping, its 'isAlive' property is set to true.
	 */
	heartbeat() {
		this.isAlive = true;
	}

	/**
	 * disconnectUser
	 * Checks if a user is connected and disconnects them. Called when a user
	 * connects to ensure each user is connected only once.
	 * @param {*} userId: User to disconnect.
	 */
	disconnectUser(userId) {
		this.wss.clients.forEach((client) => {
			if (client.user.userId === userId) {
				client.terminate();
			}
		});
	}

	/**
	 * verifyClient
	 * Function that the WebSocket server will use to verify new clients. Uses'
	 * the validator function that the WebSocketHandler was initialized with to
	 * retrieve the client's user object and attaches it to the info parameter to
	 * be used in the connect event handler.
	 * @param {*} info: Info about the incoming connection; contains the client JWT.
	 * @param {*} cb: Callback to call when verification is complete. Call with true
	 * 		if verification succeeds. Call with false, error code, and message if
	 * 		verification fails.
	 */
	async verifyClient(info, cb) {
		let jwt = info.req.headers.authorization.replace('Bearer ', '');
		try {
			let user = await this.validate(jwt);
			info.req.user = user;
			this.disconnectUser(user.userId);
			cb(true);
		} catch (err) {
			if (err.type == 'JWT-VALIDATION') {
				cb(false, 401, 'JWT Validation failed');
			} else if (err.type == 'USER-LOAD') {
				cb(false, 404, 'User not found');
			} else {
				logger.error(err);
				cb(false, 500, 'Unexpected error.');
			}
		}
	}

	/**
	 * processChatMessage
	 * Event handler for chat messages. Receives a message and sends it to all
	 * users.
	 * @param {*} message: JSON string with the WSClientMessage containing a
	 * 		ClientServerChatMessage.
	 * @param {*} client: the client object of the sender.
	 */
	async processChatMessage(message, client) {
		var utc = new Date().toJSON();
		let chatMessage = await Models.ChatMessage.create({
			message: message.message,
			timestamp: utc
		});

		await chatMessage.setUser(client.user);
		chatMessage.User = client.user;

		let serverChatMessage = new ServerMessages.ServerClientChatMessage(chatMessage);
		let outgoingMessage = JSON.stringify(serverChatMessage.toServerMessage());

		this.wss.clients.forEach((sclient) => {
			sclient.send(outgoingMessage);
		});
	}

	/**
	 * Function for processing messages from the trivia host.
	 * @param {*} clientMessage the HostServerMessage to be processed
	 * @param {*} client the client object of the sender, should also contain the User object
	 * 				which is attached when they connect.
	 */
	async processTriviaHostMessage(clientMessage, client) {
		// Validate that user is a host.
		if (client.user.Role.roleName !== 'Host') {
			this.sendError('User is not a host', client);
			return;
		}

		// Validate that message type is valid.
		if (!ClientMessages.HostServerMessage.MESSAGE_TYPES[clientMessage.messageType]) {
			this.sendError(`Invalid HostServerMessageType: ${clientMessage.messageType}`, client);
			return;
		}

		// Handle each message type.
		if (clientMessage.messageType === ClientMessages.HostServerMessage.MESSAGE_TYPES.OpenGame) {
			if (this.activeTrivia) {
				this.sendError(`There is already an active trivia game.`, client);
				return;
			}
			let triviaGame = await Models.TriviaGame.findWithImages(clientMessage.payload.gameId);

			if (!triviaGame) {
				this.sendError(`Trivia game with id ${clientMessage.payload.gameId} not found.`, client);
				return;
			}

			// add the hosts client to it's user object for easier access in the future.
			let host = client.user;
			host.client = client;
			this.activeTrivia = new ActiveTriviaGame(triviaGame, host);

			this.wss.clients.forEach((sclient) => {
				this.sendGameInfo(sclient);
			})
		} else {
			// All other messages require a game to be Open and only the host
			// that opened the game should have access.
			if (client.user.id !== this.activeTrivia.host.id) {
				this.sendError('Another host is controlling this game', client);
				return;
			}
			if (clientMessage.messageType === ClientMessages.HostServerMessage.MESSAGE_TYPES.EndGame) {
				this.activeTrivia = null;
				this.wss.clients.forEach((sclient) => {
					this.sendGameInfo(sclient);
				});
			} else if (clientMessage.messageType === ClientMessages.HostServerMessage.MESSAGE_TYPES.StartTrivia) {
				// Start the game and send the game data out to players.
				let game = this.activeTrivia.startGame();
				let response = new ServerMessages.ServerHostMessage(
					ServerMessages.ServerHostMessage.MESSAGE_TYPES.TriviaStart, game).toServerMessage();
				this.activeTrivia.host.client.send(JSON.stringify(response));

				response = JSON.stringify(new ServerMessages.ServerPlayerMessage(
					ServerMessages.ServerPlayerMessage.MESSAGE_TYPES.TriviaStart, game).toServerMessage());
				this.sendToPlayers(response);
			} else if (clientMessage.messageType === ClientMessages.HostServerMessage.MESSAGE_TYPES.Next) {
				// Transition to the next "slide" of the game, different results have different responses.
				try {
					let next = await this.activeTrivia.next();
					if (next.type === 'round') {
						// Send next round message to players and host
						let response = new ServerMessages.ServerHostMessage(
							ServerMessages.ServerHostMessage.MESSAGE_TYPES.RoundStart, next.round).toServerMessage();
						this.activeTrivia.host.client.send(JSON.stringify(response));

						response = JSON.stringify(new ServerMessages.ServerPlayerMessage(
							ServerMessages.ServerPlayerMessage.MESSAGE_TYPES.RoundStart, next.round).toServerMessage());
						this.sendToPlayers(response);
					} else if (next.type === 'question') {
						// Send next question message to players and host
						let response = new ServerMessages.ServerHostMessage(
							ServerMessages.ServerHostMessage.MESSAGE_TYPES.Question, next.question).toServerMessage();
						this.activeTrivia.host.client.send(JSON.stringify(response));

						response = JSON.stringify(new ServerMessages.ServerPlayerMessage(
							ServerMessages.ServerPlayerMessage.MESSAGE_TYPES.Question, next.question).toServerMessage());
						this.sendToPlayers(response);
					} else if (next.type === 'grading') {
						// Send out assigned answers to leaders of teams.
						for (let i = 0; i < next.assignments.length; i++) {
							let response = new ServerMessages.ServerPlayerMessage(
								ServerMessages.ServerHostMessage.MESSAGE_TYPES.Grading, {
									question: next.question,
									teamAnswers: next.assignments[i].teamAnswers
								}).toServerMessage();
							this.sendToUser(JSON.stringify(response), next.assignments[i].team.teamLeader);
						}

						// Notify host that grading has begun
						let response = new ServerMessages.ServerHostMessage(
							ServerMessages.ServerHostMessage.MESSAGE_TYPES.Grading, next.question).toServerMessage();
						this.activeTrivia.host.client.send(JSON.stringify(response));
					} else if (next.type === 'end') {
						// If game has ended, reset the game and send empty game message.
						this.activeTrivia = null;
						this.wss.clients.forEach((sclient) => {
							this.sendGameInfo(sclient);
						})
					} else if (next.type === 'scoreboard') {
						// Send scores to players and host.
						let response = new ServerMessages.ServerPlayerMessage(
							ServerMessages.ServerPlayerMessage.MESSAGE_TYPES.Scores,
							next.scores
						).toServerMessage();
						this.sendToPlayers(JSON.stringify(response));
						this.activeTrivia.host.client.send(JSON.stringify(response));
					}
				} catch (err) {
					logger.error(`Error: ${err}`);
					this.sendError('Game not started', client);
				}

			} else {
				this.sendError(`Host message type: ${clientMessage.messageType} not handled.`, client);
			}
		}
	}

	/**
	 * Function for processing player messages.
	 * @param {*} clientMessage The PlayerServerMessage object
	 * @param {*} client the sender's client object which should include
	 * 		their User object.
	 */
	async processPlayerMessage(clientMessage, client) {
		// Cannot process messages if their is no active game
		if (!this.activeTrivia) {
			this.sendError('No active game of trivia.', client);
			return;
		}

		if (clientMessage.messageType === ClientMessages.PlayerServerMessage.MESSAGE_TYPES.TableStatusRequest) {
			// Handle the TableStatusRequest

			// Load the table with the sent QRCode
			let qrCode = clientMessage.payload.QRCode;
			let table = await Models.Table.find({
				where: {
					qrCode: qrCode
				}
			});


			if (table) {
				// Check if their is already a team on the table
				for (let i = 0; i < this.activeTrivia.teams.length; i++) {
					if (this.activeTrivia.teams[i].Table.qrCode === qrCode) {
						let response;
						// Respond that the team is open to join if their are less
						// than 6 players, or full if  >= 6 players.
						if (this.activeTrivia.teams[i].Users.length >= 6) {
							response = new ServerMessages.ServerPlayerMessage(
								ServerMessages.ServerPlayerMessage.MESSAGE_TYPES.TableStatusResponse, {
									QRCode: qrCode,
									status: 'team full'
								});
						} else {
							response = new ServerMessages.ServerPlayerMessage(
								ServerMessages.ServerPlayerMessage.MESSAGE_TYPES.TableStatusResponse, {
									QRCode: qrCode,
									status: 'team open',
									team: {
										teamLeader: this.activeTrivia.teams[i].teamLeader,
										teamName: this.activeTrivia.teams[i].teamName
									}
								});
						}
						client.send(JSON.stringify(response.toServerMessage()));
						return;
					}
				}

				// Respond that their is no team for the table
				let response = new ServerMessages.ServerPlayerMessage(
					ServerMessages.ServerPlayerMessage.MESSAGE_TYPES.TableStatusResponse, {
						QRCode: qrCode,
						status: 'no team'
					});
				client.send(JSON.stringify(response.toServerMessage()));
			} else {
				this.sendError(`Table with QR Code: ${clientMessage.payload.QRCode} not found.`, client);
			}
		} else if (clientMessage.messageType === ClientMessages.PlayerServerMessage.MESSAGE_TYPES.CreateTeam) {
			// Load the table to create the team for
			let qrCode = clientMessage.payload.QRCode;
			let table = await Models.Table.find({
				where: {
					qrCode: qrCode
				}
			});

			if (table) {
				for (let i = 0; i < this.activeTrivia.teams.length; i++) {
					// Check that a team doesn't exist for this table
					if (this.activeTrivia.teams[i].Table.qrCode === qrCode) {
						let response = new ServerMessages.ServerPlayerMessage(
							ServerMessages.ServerPlayerMessage.MESSAGE_TYPES.CreateTeamResponse, {
								QRCode: qrCode,
								teamName: clientMessage.payload.teamName,
								success: false,
								reason: 'Team already exists for table'
							});
						client.send(JSON.stringify(response.toServerMessage()));
						return;
					}

					// check that the user isn't on another team
					for (let j = 0; j < this.activeTrivia.teams[i].Users.length; j++) {
						if (client.user.id === this.activeTrivia.teams[i].Users[j].id) {
							let response = new ServerMessages.ServerPlayerMessage(
								ServerMessages.ServerPlayerMessage.MESSAGE_TYPES.CreateTeamResponse, {
									QRCode: qrCode,
									teamName: clientMessage.payload.teamName,
									success: false,
									reason: 'User already belongs to a team'
								});
							client.send(JSON.stringify(response.toServerMessage()));
							return;
						}
					}
				}

				// Create the team and associate with user, table and game
				let team = await Models.Team.create({
					teamName: clientMessage.payload.teamName
				});
				await team.setTeamLeader(client.user);
				await team.addUser(client.user);
				await team.setTable(table);
				await team.setTriviaGame(this.activeTrivia.triviaGame);
				// reload with associations since they are not added to object
				team = await Models.Team.findById(team.id, {
					include: [{
							model: Models.User,
							as: 'Users',
							through: 'TeamToUser'
						},
						{
							model: Models.User,
							as: 'teamLeader',
							foreignKey: 'teamLeaderId'
						},
						Models.Table

					]
				});
				this.activeTrivia.addTeam(team);

				// Send the success response to the user
				let response = new ServerMessages.ServerPlayerMessage(
					ServerMessages.ServerPlayerMessage.MESSAGE_TYPES.CreateTeamResponse, {
						QRCode: clientMessage.payload.QRCode,
						success: true,
						teamName: clientMessage.payload.teamName
					});
				client.send(JSON.stringify(response.toServerMessage()));

			} else {
				let response = new ServerMessages.ServerPlayerMessage(
					ServerMessages.ServerPlayerMessage.MESSAGE_TYPES.CreateTeamResponse, {
						QRCode: qrCode,
						teamName: clientMessage.payload.teamName,
						success: false,
						reason: `No table with qrCode ${qrCode} found.`
					});
				client.send(JSON.stringify(response.toServerMessage()));
			}

		} else if (clientMessage.messageType === ClientMessages.PlayerServerMessage.MESSAGE_TYPES.JoinTeam) {
			// Load the table to player wants to join the team for
			let qrCode = clientMessage.payload.QRCode;
			let table = await Models.Table.find({
				where: {
					qrCode: qrCode
				}
			});

			if (table) {
				let teamNum;
				for (let i = 0; i < this.activeTrivia.teams.length; i++) {
					// Check that a team doesn't exist for this table
					if (this.activeTrivia.teams[i].Table.qrCode === qrCode) {
						teamNum = i;
					}

					// check that the user isn't already on team
					for (let j = 0; j < this.activeTrivia.teams[i].Users.length; j++) {
						if (client.user.id === this.activeTrivia.teams[i].Users[j].id) {
							let response = new ServerMessages.ServerPlayerMessage(
								ServerMessages.ServerPlayerMessage.MESSAGE_TYPES.JoinTeamResponse, {
									QRCode: qrCode,
									success: false,
									reason: 'User already belongs to a team'
								});
							client.send(JSON.stringify(response.toServerMessage()));
							return;
						}
					}
				}

				// If no team found, then return failure
				if (teamNum === undefined) {
					let response = new ServerMessages.ServerPlayerMessage(
						ServerMessages.ServerPlayerMessage.MESSAGE_TYPES.JoinTeamResponse, {
							QRCode: qrCode,
							success: false,
							reason: 'No matching team found for table'
						});
					client.send(JSON.stringify(response.toServerMessage()));
					return;
				}

				let team = this.activeTrivia.teams[teamNum];
				await team.addUser(client.user);

				// reload with associations since they are not added to object
				team = await Models.Team.findById(team.id, {
					include: [{
							model: Models.User,
							as: 'Users',
							through: 'TeamToUser'
						},
						{
							model: Models.User,
							as: 'teamLeader',
							foreignKey: 'teamLeaderId'
						},
						Models.Table

					]
				});
				this.activeTrivia.teams[teamNum] = team;

				// Send a success response to the player.
				let response = new ServerMessages.ServerPlayerMessage(
					ServerMessages.ServerPlayerMessage.MESSAGE_TYPES.JoinTeamResponse, {
						QRCode: qrCode,
						teamName: team.teamName,
						success: true
					});
				client.send(JSON.stringify(response.toServerMessage()));

			} else {
				let response = new ServerMessages.ServerPlayerMessage(
					ServerMessages.ServerPlayerMessage.MESSAGE_TYPES.JoinTeamResponse, {
						QRCode: qrCode,
						teamName: clientMessage.payload.teamName,
						success: false,
						reason: `No table with qrCode ${qrCode} found.`
					});
				client.send(JSON.stringify(response.toServerMessage()));
			}
		} else if (clientMessage.messageType === ClientMessages.PlayerServerMessage.MESSAGE_TYPES.AnswerQuestion) {
			// Find team that player belongs to.
			let team;
			for (let i = 0; i < this.activeTrivia.teams.length && team === undefined; i++) {
				for (let j = 0; j < this.activeTrivia.teams[i].Users.length && team === undefined; j++) {
					if (this.activeTrivia.teams[i].Users[j].id === client.user.id) {
						team = i;
					}
				}
			}
			if (team === undefined) {
				this.sendError('Player does not belong to a team', client);
				return;
			}

			// Validate question number matches current question and send to team leader.
			if (clientMessage.payload.roundNumber === this.activeTrivia.currentRound &&
				clientMessage.payload.questionNumber === this.activeTrivia.currentQuestion) {
				let message = new ServerMessages.ServerPlayerMessage(ServerMessages.ServerPlayerMessage.MESSAGE_TYPES.AnswerSubmission, clientMessage.payload).toServerMessage();
				let leader = this.activeTrivia.teams[team].teamLeader;
				this.sendToUser(JSON.stringify(message), leader);
			} else {
				this.sendError('Invalid question number', client);
			}
		} else if (clientMessage.messageType === ClientMessages.PlayerServerMessage.MESSAGE_TYPES.FinalAnswer) {
			// find team that player belongs to
			let teamIndex;
			for (let i = 0; i < this.activeTrivia.teams.length && teamIndex === -1; i++) {
				if (this.activeTrivia.teams[i].teamLeader.id === client.user.id) {
					teamIndex = i;
				}
			}

			if (teamIndex === undefined) {
				let response = new ServerMessages.ServerPlayerMessage(
					ServerMessages.ServerPlayerMessage.MESSAGE_TYPES.FinalAnswerResponse, {
						roundNumber: this.activeTrivia.currentRound,
						questionNumber: this.activeTrivia.currentQuestion,
						answer: clientMessage.payload.answer,
						success: false,
						reason: 'Submitter, not team leader'
					}
				).toServerMessage();
				client.send(JSON.stringify(response));
				return;
			}

			// Validate question matches current question.
			if (clientMessage.payload.roundNumber === this.activeTrivia.currentRound &&
				clientMessage.payload.questionNumber === this.activeTrivia.currentQuestion) {

				let question = this.activeTrivia.triviaGame.triviaRounds[this.activeTrivia.currentRound].triviaQuestions[this.activeTrivia.currentQuestion];
				let team = this.activeTrivia.teams[teamIndex];

				// Check if the team has already submitted an answer.
				let previousAnswer = await Models.TeamAnswer.find({
					where: {
						teamId: team.id,
						triviaQuestionId: question.id
					}
				});

				if (previousAnswer) {
					let response = new ServerMessages.ServerPlayerMessage(
						ServerMessages.ServerPlayerMessage.MESSAGE_TYPES.FinalAnswerResponse, {
							roundNumber: this.activeTrivia.currentRound,
							questionNumber: this.activeTrivia.currentQuestion,
							answer: clientMessage.payload.answer,
							success: false,
							reason: 'Team already answered'
						}
					).toServerMessage();
					client.send(JSON.stringify(response));
					return;
				}

				// Store team's answer in database
				let answer = await Models.TeamAnswer.create({
					answer: clientMessage.payload.answer
				});
				await answer.setTriviaQuestion(question);
				await answer.setTeam(team);

				this.activeTrivia.teamsSubmitted++;

				// Send update message to host
				let hostMessage = new ServerMessages.ServerHostMessage(
					ServerMessages.ServerHostMessage.MESSAGE_TYPES.AnswerStatus, {
						roundNumber: this.activeTrivia.currentRound,
						questionNumber: this.activeTrivia.currentQuestion,
						numTeams: this.activeTrivia.teams.length,
						answersSubmitted: this.activeTrivia.teamsSubmitted
					}
				).toServerMessage();
				this.sendToUser(JSON.stringify(hostMessage), this.activeTrivia.host);

				// Send a message to players on a team with answer response.
				let teamMessage = JSON.stringify(new ServerMessages.ServerPlayerMessage(
					ServerMessages.ServerPlayerMessage.MESSAGE_TYPES.FinalAnswerResponse, {
						roundNumber: this.activeTrivia.currentRound,
						questionNumber: this.activeTrivia.currentQuestion,
						answer: clientMessage.payload.answer,
						success: true
					}
				).toServerMessage());

				for (let i = 0; i < team.Users.length; i++) {
					this.sendToUser(teamMessage, team.Users[i]);
				}

			} else {
				this.sendError('Invalid question number', client);
			}
		} else if (clientMessage.messageType === ClientMessages.PlayerServerMessage.MESSAGE_TYPES.GradeQuestion) {
			try {
				// Send the grades to ActiveTriviaGame for processing
				await this.activeTrivia.submitGrades(clientMessage.payload, client.user);
				// Send update to host
				let response = new ServerMessages.ServerHostMessage(
					ServerMessages.ServerHostMessage.MESSAGE_TYPES.GradingStatus, {
						roundNumber: this.activeTrivia.currentRound,
						questionNumber: this.activeTrivia.currentQuestion,
						numTeams: this.activeTrivia.teams.length,
						gradesSubmitted: this.activeTrivia.teamsSubmitted
					}
				).toServerMessage();

				this.sendToUser(JSON.stringify(response), this.activeTrivia.host);
			} catch (err) {
				this.sendError(err, client);
			}

		} else {
			this.sendError(`Player message type: ${clientMessage.messageType} not handled.`, client);
		}
	}

	/**
	 * sendAllMessages
	 * Function for sending all chat messages to a newly connected client. Sends
	 * a JSON string containing a WSServerMessage containing a
	 * ServerClientChatMessage with an array of messages.
	 * @param {*} client: The client to send all messages to.
	 */
	async sendAllMessages(client) {
		let messages;
		try {
			messages = await Models.ChatMessage.findAll({
				include: Models.User
			});
		} catch (err) {
			this.sendError(client, 'Unable to retrieve message history.');
			return;
		}

		let serverChatMessage = new ServerMessages.ServerClientChatMessage(messages);
		let outgoingMessage = JSON.stringify(serverChatMessage.toServerMessage());
		client.send(outgoingMessage);
	}

	/**
	 * Sends the current game status to a client.
	 * @param {*} client the client to send the status to.
	 */
	sendGameInfo(client) {
		let status;
		if (this.activeTrivia) {
			// Get status from ActiveTriviaGame
			let gameInfo = this.activeTrivia.gameInfo;
			logger.debug(`GameInfo: ${gameInfo}`);
			status = {
				status: "open",
				game: gameInfo
			};
		} else {
			status = {
				status: 'closed'
			};
		}
		let serverMessage = new ServerMessages.ServerPlayerMessage(
			ServerMessages.ServerPlayerMessage.MESSAGE_TYPES.GameInfo, status).toServerMessage();
		client.send(JSON.stringify(serverMessage));
	}

	/**
	 * Sends a message with type 'Error' and a payload with an error message
	 * @param {*} errorMessage The message for the payload.
	 * @param {*} client The client to send error to.
	 */
	sendError(errorMessage, client) {
		let error = {
			messageType: 'Error',
			payload: {
				error: errorMessage
			}
		};
		logger.error(error);
		client.send(JSON.stringify(error));
	}

	/**
	 * Sends a message to all clients connected to the server except the host
	 * of the current game.
	 * @param {*} message The message to send out.
	 */
	sendToPlayers(message) {
		let hostId = this.activeTrivia.host.id;
		this.wss.clients.forEach((sclient) => {
			if (sclient.user.id !== hostId) {
				sclient.send(message);
			}
		});
	}

	/**
	 * Sends a message to a specific user.
	 * @param {*} message The message to send to the user.
	 * @param {*} user The User object of the user to send to.
	 */
	sendToUser(message, user) {
		this.wss.clients.forEach((sclient) => {
			if (sclient.user.id === user.id) {
				sclient.send(message);
			}
		});
	}
}

module.exports = WebSocketHandler;