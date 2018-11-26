'use strict';
module.exports = (sequelize, DataTypes) => {
	const Team = sequelize.define('Team', {
		teamName: DataTypes.STRING
	}, {});
	Team.associate = function (models) {
		// associations can be defined here
		Team.belongsTo(models.Table);
		Team.belongsToMany(models.User, {
			as: 'Users',
			through: 'TeamToUser'
		});
		Team.belongsTo(models.User, {
			as: 'teamLeader',
			foreignKey: 'teamLeaderId'
		});
		Team.hasMany(models.TeamAnswer);
		Team.hasMany(models.AnswerGrade, {
			as: 'gradingTeam',
			foreignKey: 'gradingTeamId'
		});
		Team.belongsTo(models.TriviaGame);
	};
	return Team;
};