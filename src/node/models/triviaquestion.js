'use strict';
module.exports = (sequelize, DataTypes) => {
	const TriviaQuestion = sequelize.define('TriviaQuestion', {
		questionNumber: DataTypes.INTEGER,
		title: DataTypes.STRING,
		text: DataTypes.STRING,
		image: DataTypes.STRING,
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
		json.image = this.image;
		json.answer = this.answer;
		return json;
	};

	return TriviaQuestion;
};