'use strict';

class ActiveTriviaGame {
	constructor(triviaGame) {
		this.triviaGame = triviaGame;
		this.started = false;
		this.grading = false;
		this.currentRound = 0;
		this.currentQuestion = 0;
		this.teams = [];
		this.gradesAwaiting = 0;
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
}

module.exports = ActiveTriviaGame;