'use strict';
module.exports = (sequelize, DataTypes) => {
	const Team = sequelize.define('Team', {
		teamName: DataTypes.STRING
	}, {});
	Team.associate = function (models) {
		// associations can be defined here
		Team.belongsTo(models.Table);
		Team.belongsToMany(models.User, {
			through: 'TeamToUser'
		});
		Team.belongsTo(models.User, {
			as: 'teamLeader'
		});
		Team.hasMany(models.TeamAnswer);
		Team.hasMany(models.AnswerGrade, {
			as: 'gradingTeam'
		});
		Team.belongsTo(models.TriviaGame);
	};
	return Team;
};