'use strict';
module.exports = (sequelize, DataTypes) => {
	const TriviaRound = sequelize.define('TriviaRound', {
		roundNumber: DataTypes.INTEGER,
		title: DataTypes.STRING,
		text: DataTypes.STRING,
		imageId: DataTypes.STRING
	}, {});
	TriviaRound.associate = function (models) {
		// associations can be defined here
		TriviaRound.belongsTo(models.TriviaGame);
		TriviaRound.hasMany(models.TriviaQuestion, {
			as: 'triviaQuestions'
		});
	};

	TriviaRound.prototype.toJSON = function () {
		let json = {};
		json.id = this.id;
		json.roundNumber = this.roundNumber;
		json.title = this.title;
		json.text = this.text;
		json.imageId = this.imageId;

		if (this.image) {
			json.image = this.image;
		}

		if (this.triviaQuestions) {
			let questions = [];
			for (let i = 0; i < this.triviaQuestions.length; i++) {
				questions.push(this.triviaQuestions[i].toJSON());
			}
			json.triviaQuestions = questions;
		}

		return json;
	};

	TriviaRound.prototype.loadImages = async function () {
		const imageStore = require('../utils/imageStore');

		if (this.imageId) {
			this.image = await imageStore.get(this.imageId);
		}
		if (this.triviaQuestions) {
			for (let i = 0; i < this.triviaQuestions.length; i++) {
				await this.triviaQuestions[i].loadImages();
			}
		}
	}
	return TriviaRound;
};