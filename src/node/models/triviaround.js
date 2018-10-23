'use strict';
module.exports = (sequelize, DataTypes) => {
	const TriviaRound = sequelize.define('TriviaRound', {
		roundNumber: DataTypes.INTEGER,
		title: DataTypes.STRING,
		text: DataTypes.STRING,
		image: DataTypes.STRING
	}, {});
	TriviaRound.associate = function (models) {
		// associations can be defined here
		TriviaRound.belongsTo(models.TriviaGame);
		TriviaRound.hasMany(models.TriviaQuestion, {
			as: 'triviaQuestions'
		});
	};
	return TriviaRound;
};