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
	return TriviaQuestion;
};