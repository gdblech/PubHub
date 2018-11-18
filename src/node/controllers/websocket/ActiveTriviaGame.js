'use strict';

class ActiveTriviaGame {
	constructor(triviaGame, host) {
		this.triviaGame = triviaGame;
		this.host = host;
		this.started = false;
		this.grading = false;
		this.onScoreboard = false;
		this.currentRound = -1;
		this.currentQuestion = -1;
		this.teams = [];
		this.gradesAwaiting = 0;
		this.teamsAnswered = 0;
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

	next() {
		// Game title screen
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
			if (this.grading) {
				this.teamsAnswered = 0;
				return {
					type: 'grading',
					question
				};
			}
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
				return {
					type: 'scoreboard'
				};
			}
			this.grading = true;
			this.currentQuestion = -1;
		}

		this.currentQuestion += 1;
		let question = this.triviaGame.triviaRounds[this.currentRound].triviaQuestions[this.currentQuestion].toJSON();
		if (this.grading) {
			this.teamsAnswered = 0;
			return {
				type: 'grading',
				question
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
}

module.exports = ActiveTriviaGame;