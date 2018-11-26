'use strict';

const _ = require('lodash');
const Models = require('../../models');
class ActiveTriviaGame {
	constructor(triviaGame, host, wss) {
		this.triviaGame = triviaGame;
		this.host = host;
		this.started = false;
		this.grading = false;
		this.onScoreboard = false;
		this.currentRound = -1;
		this.currentQuestion = -1;
		this.teams = [];
		this.gradesAwaiting = 0;
		this.teamsSubmitted = 0;
		this.wss = wss;
		this.currentAssignments = null;
	}

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



	startGame() {
		if (this.started) {
			throw 'Game already started';
		}
		this.started = true;
		let game = this.triviaGame.toJSON();
		delete game.triviaRounds;
		return game;
	}

	async next() {
		// Game title screen
		if (!this.started) {
			throw 'Game not started';
		}
		if (this.currentRound === -1) {
			this.currentRound = 0;
			let round = this.triviaGame.triviaRounds[this.currentRound].toJSON();
			delete round.triviaQuestions;
			return {
				type: 'round',
				round
			};
		}

		// Round title screen
		if (this.currentQuestion === -1) {
			this.currentQuestion = 0;
			let question = this.triviaGame.triviaRounds[this.currentRound].triviaQuestions[this.currentQuestion].toJSON();
			question.roundNumber = this.currentRound;
			this.teamsSubmitted = 0;
			delete question.answer;
			return {
				type: 'question',
				question
			};
		}

		// On scoreboard
		if (this.onScoreboard) {
			if (this.currentRound === this.triviaGame.triviaRounds.length - 1) {
				// TODO: End of game
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

		// Last question of a round
		if (this.currentQuestion === this.triviaGame.triviaRounds[this.currentRound].triviaQuestions.length - 1) {
			// Round grading
			if (this.grading) {
				// TODO: goto scoreboard
				this.grading = false;
				this.onScoreboard = true;
				this.currentQuestion = -1;
				this.currentRound++;
				this.currentAssignments = null;
				return {
					type: 'scoreboard'
				};
			}
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

		this.currentQuestion += 1;
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

	addTeam(team) {
		this.teams.push(team);
	}

	async assignAnswers() {
		let rotate1;
		let rotate2;
		if (this.teams.length > 2) {
			rotate1 = _.random(1, this.teams.length - 1);
			rotate2 = _.random(1, this.teams.length - 2);
			if (rotate2 >= rotate1) {
				rotate2++;
			}
		} else {
			rotate1 = this.teams.length - 1;
		}

		let assignments = [];

		for (let i = 0; i < this.teams.length; i++) {
			let teamIndex1;
			let teamIndex2;
			let answer1;
			let answer2;

			teamIndex1 = (i + rotate1) % this.teams.length;
			answer1 = await Models.TeamAnswer.find({
				where: {
					teamId: this.teams[teamIndex1].id,
					triviaQuestionId: this.triviaGame.triviaRounds[this.currentRound].triviaQuestions[this.currentQuestion].id
				}
			});

			if (this.teams.length > 2) {
				teamIndex2 = (i + rotate2) % this.teams.length;
				answer2 = await Models.TeamAnswer.find({
					where: {
						teamId: this.teams[teamIndex2].id,
						triviaQuestionId: this.triviaGame.triviaRounds[this.currentRound].triviaQuestions[this.currentQuestion].id
					}
				});
			}
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

	async submitGrades(submission, user) {

		if (submission.roundNumber !== this.currentRound || submission.questionNumber !== this.currentQuestion) {
			throw 'Invalid question number';
		}

		let team;
		for (let i = 0; i < this.teams.length; i++) {
			if (this.teams[i].teamLeader.id === user.id) {
				team = this.teams[i];
			}
		}

		if (team === undefined) {
			throw 'Submitter not leader of a team';
		}

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
				throw 'Team id(s) does not match assignment'
			}
		} else {
			if (submission.teamGrades[0].teamId === assignment.teamAnswers[0].teamId) {
				if (submission.teamGrades[1].teamId !== assignment.teamAnswers[1].teamId) {
					throw 'Team id(s) does not match assignment'
				}
			} else if (submission.teamGrades[0].teamId === assignment.teamAnswers[1].teamId) {
				if (submission.teamGrades[1].teamId !== assignment.teamAnswers[0].teamId) {
					throw 'Team id(s) does not match assignment'
				}
			} else {
				throw 'Team id(s) does not match assignment'
			}
		}
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