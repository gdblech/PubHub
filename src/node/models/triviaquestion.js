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
		json.questionNumber = this.roundNumber;
		json.title = this.title;
		json.text = this.text;
		json.image = this.image;
		return json;
	};

	return TriviaQuestion;
};