'use strict';
module.exports = (sequelize, DataTypes) => {
	const TriviaQuestion = sequelize.define('TriviaQuestion', {
		questionNumber: DataTypes.INTEGER,
		title: DataTypes.STRING,
		text: DataTypes.STRING,
		imageId: DataTypes.STRING,
		answer: DataTypes.STRING
	}, {});
	TriviaQuestion.associate = function (models) {
		// associations can be defined here
		TriviaQuestion.hasMany(models.TeamAnswer);
		TriviaQuestion.belongsTo(models.TriviaRound);
	};

	TriviaQuestion.prototype.toJSON = function () {
		let json = {};
		json.id = this.id;
		json.questionNumber = this.questionNumber;
		json.title = this.title;
		json.text = this.text;
		json.imageId = this.imageId;
		json.answer = this.answer;

		if (this.image) {
			json.image = this.image
		}

		if (this.roundNumber) {
			json.roundNumber = this.roundNumber;
		}
		return json;
	};

	TriviaQuestion.prototype.loadImages = async function () {
		const imageStore = require('../utils/imageStore');

		if (this.imageId) {
			this.image = await imageStore.get(this.imageId);
		}
	}
	return TriviaQuestion;
};