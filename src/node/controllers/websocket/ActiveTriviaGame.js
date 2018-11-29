'use strict';

const _ = require('lodash');
const Models = require('../../models');
const log4js = require('log4js');
let logger = log4js.getLogger();
logger.level = process.env.LOG_LEVEL || 'info';

/**
 * Class for managing the active trivia game. Maintains the game state and controls
 * progress through the game.
 */
class ActiveTriviaGame {

	/**
	 * Constructor for the active trivia game, sets up the game state for the trivia game
	 * @param {*} triviaGame a TriviaGame object of the game to be played
	 * @param {*} host a User object for the host of the game
	 */
	constructor(triviaGame, host) {
		this.triviaGame = triviaGame;
		this.host = host;
		this.teams = [];

		// variables for tracking current game state
		this.started = false;
		this.grading = false;
		this.onScoreboard = false;
		this.currentRound = -1;
		this.currentQuestion = -1;

		// Track the number of teams that have submitted (used for answers and grades)
		this.teamsSubmitted = 0;

		// Track the answers assigned to each team for grading
		this.currentAssignments = null;
	}

	/**
	 * Returns an object with the current game status to be sent to new players.
	 */
	get gameInfo() {
		let info = {};
		info.title = this.triviaGame.title;
		info.text = this.triviaGame.text;
		info.image = this.triviaGame.image;
		info.hostName = this.hostName;
		info.roundNumber = this.roundNumber;
		info.questionNumber = this.questionNumber;
		info.started = this.started;
		info.grading = this.grading;
		if (this.currentRound > 0) {
			// TODO: add stuff after round started.
		}
		return info;
	}

	/**
	 * Function to transition the game into the started state.
	 */
	startGame() {
		if (this.started) {
			throw 'Game already started';
		}
		this.started = true;
		let game = this.triviaGame.toJSON();
		delete game.triviaRounds;
		return game;
	}

	/**
	 * Function that processes main game logic. Checks game states and moves the game
	 * throughout the states of the game until it is complete. Returns an object
	 * containing the information for the next slide of the game.
	 */
	async next() {
		if (!this.started) {
			throw 'Game not started';
		}

		// Game is on the title screen, begin first round
		if (this.currentRound === -1) {
			this.currentRound = 0;
			let round = this.triviaGame.triviaRounds[this.currentRound].toJSON();
			delete round.triviaQuestions;
			return {
				type: 'round',
				round
			};
		}

		// On scoreboard.
		if (this.onScoreboard) {
			// End game if this is the last round.
			if (this.currentRound === this.triviaGame.triviaRounds.length - 1) {
				this.triviaGame.completed = true;
				await this.triviaGame.save();
				return {
					type: 'end'
				};
			}

			// Start next round
			this.currentRound += 1;
			this.currentQuestion = -1;
			this.onScoreboard = false;
			let round = this.triviaGame.triviaRounds[this.currentRound].toJSON();
			delete round.triviaQuestions;
			return {
				type: 'round',
				round
			};
		}

		// If in grading phase, mark each answer as correct or false
		if (this.grading) {
			let answers = await Models.TeamAnswer.findAll({
				where: {
					triviaQuestionId: this.triviaGame.triviaRounds[this.currentRound].triviaQuestions[this.currentQuestion].id
				},
				include: [{
					model: Models.AnswerGrade
				}]
			});

			for (let i = 0; i < answers.length; i++) {
				let correct = false;
				for (let j = 0; j < answers[i].AnswerGrades.length; j++) {
					if (answers[i].AnswerGrades[j].correct) {
						correct = true;
					}
				}
				answers[i].correct = correct;
				await answers[i].save();
			}
		}

		// Last question of a round
		if (this.currentQuestion === this.triviaGame.triviaRounds[this.currentRound].triviaQuestions.length - 1) {
			// When in grading, progress to scoreboard
			if (this.grading) {
				this.grading = false;
				this.onScoreboard = true;
				this.currentAssignments = null;
				let teams = await Models.Team.findAll({
					where: {
						triviaGameId: this.triviaGame.id
					},
					include: [
						Models.TeamAnswer
					]
				});

				// Calculate scores
				let scores = {
					roundNumber: this.currentRound,
					teamScores: []
				};
				for (let i = 0; i < teams.length; i++) {
					let score = 0;
					for (let j = 0; j < teams[i].TeamAnswers.length; j++) {
						if (teams[i].TeamAnswers[j].correct === true) {
							score++;
						}
					}
					scores.teamScores.push({
						teamName: teams[i].teamName,
						score
					})
				}

				return {
					type: 'scoreboard',
					scores
				};
			}

			// If not grading, start grading phase.
			this.grading = true;
			this.currentQuestion = 0;
			this.teamsSubmitted = 0;

			let question = this.triviaGame.triviaRounds[this.currentRound].triviaQuestions[this.currentQuestion].toJSON();
			question.roundNumber = this.currentRound;
			let assignments = await this.assignAnswers();
			return {
				type: 'grading',
				question,
				assignments
			};
		}

		// Send the information for the next question (answer of grading phase)
		this.currentQuestion++;
		let question = this.triviaGame.triviaRounds[this.currentRound].triviaQuestions[this.currentQuestion].toJSON();
		question.roundNumber = this.currentRound;
		this.teamsSubmitted = 0;
		if (this.grading) {
			let assignments = await this.assignAnswers();
			return {
				type: 'grading',
				question,
				assignments
			};
		}

		delete question.answer;
		return {
			type: 'question',
			question
		};
	}

	/**
	 * Add a team to the list of tracked teams.
	 * @param {*} team the team to be added.
	 */
	addTeam(team) {
		this.teams.push(team);
	}

	/**
	 * Assign answers to teams for grading. If there is one team, they grade their
	 * own answer. For 2 teams, each team grades the other team's answer. For 3 or
	 * or more teams, each team grades 2 the answers from 2 random teams. Returns
	 * an array with objects containing a team and an array of assigned answers.
	 */
	async assignAnswers() {
		// Uses rotations to assign grading without risk of conflicts. (Explanation
		// in paper).
		let rotate1;
		let rotate2;

		// set rotation values.
		if (this.teams.length > 2) {
			rotate1 = _.random(1, this.teams.length - 1);
			rotate2 = _.random(1, this.teams.length - 2);
			if (rotate2 >= rotate1) {
				rotate2++;
			}
		} else {
			rotate1 = this.teams.length - 1;
		}

		// Use rotations to assign each team answers.
		let assignments = [];
		for (let i = 0; i < this.teams.length; i++) {
			let teamIndex1;
			let teamIndex2;
			let answer1;
			let answer2;

			// First assigned answer
			teamIndex1 = (i + rotate1) % this.teams.length;
			answer1 = await Models.TeamAnswer.find({
				where: {
					teamId: this.teams[teamIndex1].id,
					triviaQuestionId: this.triviaGame.triviaRounds[this.currentRound].triviaQuestions[this.currentQuestion].id
				}
			});

			// Second assigned answer, if number of teams > 2.
			if (this.teams.length > 2) {
				teamIndex2 = (i + rotate2) % this.teams.length;
				answer2 = await Models.TeamAnswer.find({
					where: {
						teamId: this.teams[teamIndex2].id,
						triviaQuestionId: this.triviaGame.triviaRounds[this.currentRound].triviaQuestions[this.currentQuestion].id
					}
				});
			}

			// Create "assignment" object and add to array
			let assignment = {
				team: this.teams[i]
			};

			assignment.teamAnswers = [];
			assignment.teamAnswers.push({
				teamId: this.teams[teamIndex1].id,
				answer: answer1.answer
			});
			if (this.teams.length > 2) {
				assignment.teamAnswers.push({
					teamId: this.teams[teamIndex2].id,
					answer: answer2.answer
				});
			}

			assignments.push(assignment);
		}
		this.currentAssignments = assignments;
		return assignments;
	}


	/**
	 * Function that validates and stores the grades from teams.
	 * @param {*} submission Object containing submitted grades along with question
	 * 		data (question/round number).
	 * @param {*} user User object for player that submitted grades.
	 */
	async submitGrades(submission, user) {

		// Validate that grades are for current question/round.
		if (submission.roundNumber !== this.currentRound || submission.questionNumber !== this.currentQuestion) {
			throw 'Invalid question number';
		}

		// Validate that submitter is the leader of a team and retrieve team object.
		let team;
		for (let i = 0; i < this.teams.length; i++) {
			if (this.teams[i].teamLeader.id === user.id) {
				team = this.teams[i];
			}
		}
		if (team === undefined) {
			throw 'Submitter not leader of a team';
		}


		// Validate that the teams that the submitter graded are the teams
		// assigned to that team.
		let assignment;
		let assignmentId;
		for (let i = 0; i < this.currentAssignments.length; i++) {
			if (team.id === this.currentAssignments[i].team.id) {
				assignment = this.currentAssignments[i];
				assignmentId = i;
			}
		}

		if (assignment === undefined) {
			throw 'Team not assigned any questions for grading'
		}

		if (submission.teamGrades.length != assignment.teamAnswers.length) {
			throw 'Incorrect number of grades submitted'
		}

		if (submission.teamGrades.length === 1) {
			if (submission.teamGrades[0].teamId !== assignment.teamAnswers[0].teamId) {
				throw 'Team id(s) do not match assignment'
			}
		} else {
			if (submission.teamGrades[0].teamId === assignment.teamAnswers[0].teamId) {
				if (submission.teamGrades[1].teamId !== assignment.teamAnswers[1].teamId) {
					throw 'Team id(s) do not match assignment'
				}
			} else if (submission.teamGrades[0].teamId === assignment.teamAnswers[1].teamId) {
				if (submission.teamGrades[1].teamId !== assignment.teamAnswers[0].teamId) {
					throw 'Team id(s) do not match assignment'
				}
			} else {
				throw 'Team id(s) do not match assignment'
			}
		}

		// Store grades to database.
		for (let i = 0; i < submission.teamGrades.length; i++) {
			let grade = await Models.AnswerGrade.create({
				correct: submission.teamGrades[i].correct
			});

			let answer = await Models.TeamAnswer.find({
				where: {
					teamId: submission.teamGrades[i].teamId,
					triviaQuestionId: this.triviaGame.triviaRounds[this.currentRound].triviaQuestions[this.currentQuestion].id
				}
			});

			await grade.setGradingTeam(team);
			await grade.setTeamAnswer(answer);
			this.currentAssignments.splice(assignmentId, 1);
			this.teamsSubmitted++;
		}
	}
}

module.exports = ActiveTriviaGame;