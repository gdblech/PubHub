'use strict';
module.exports = (sequelize, DataTypes) => {
	const TriviaGame = sequelize.define('TriviaGame', {
		date: DataTypes.DATE,
		hostName: DataTypes.STRING,
		title: DataTypes.STRING,
		text: DataTypes.STRING,
		image: DataTypes.STRING
	}, {});
	TriviaGame.associate = function (models) {
		// associations can be defined here
		TriviaGame.belongsTo(models.User, {
			as: 'host'
		});
		TriviaGame.hasMany(models.TriviaRound);
		TriviaGame.hasMany(models.Team);
	};
	return TriviaGame;
};