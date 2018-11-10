'use strict';
const ClientMessages = require('./ClientMessages');
const ServerMessages = require('./ServerMessages');
const Models = require('../../models');
const ws = require('ws');
const log4js = require('log4js');
const ActiveTriviaGame = require('./ActiveTriviaGame');
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
				this.processChatMessage(clientMessage.payload, client);
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

	async processTriviaHostMessage(clientMessage, client) {
		if (clientMessage.messageType === ClientMessages.HostServerMessage.MESSAGE_TYPES.openGame) {

			if (this.activeTrivia) {
				this.sendError(`There is already an active trivia game.`, client);
				return;
			}
			let triviaGame = await Models.TriviaGame.findWithImages(clientMessage.payload.gameId);

			if (!triviaGame) {
				this.sendError(`Trivia game with id ${clientMessage.payload.gameId} not found.`, client);
				return;
			}

			this.activeTrivia = new ActiveTriviaGame(triviaGame);

			this.wss.clients.forEach((sclient) => {
				this.sendGameInfo(sclient);
			})
		} else if (clientMessage.messageType === ClientMessages.HostServerMessage.MESSAGE_TYPES.endGame) {
			this.activeTrivia = null;
			this.wss.clients.forEach((sclient) => {
				this.sendGameInfo(sclient);
			});
		} else {
			this.sendError(`Host message type: ${clientMessage.messageType} not handled.`, client);
		}
		logger.debug(`Message: ${JSON.stringify(message)}`);
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

	async sendGameInfo(client) {
		let status;
		if (this.activeTrivia) {
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
		let serverMessage = new ServerMessages.ServerPlayerMessage("gameInfo", status).toServerMessage();
		client.send(JSON.stringify(serverMessage));
	}

	async sendError(errorMessage, client) {
		let error = {
			messageType: 'Error',
			error: errorMessage
		};
		logger.error(error);
		client.send(JSON.stringify(error));
	}
}

module.exports = WebSocketHandler;