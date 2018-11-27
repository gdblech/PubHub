'use strict';
module.exports = (sequelize, DataTypes) => {
	const AnswerGrade = sequelize.define('AnswerGrade', {
		correct: DataTypes.BOOLEAN
	}, {});
	AnswerGrade.associate = function (models) {
		// associations can be defined here
		AnswerGrade.belongsTo(models.TeamAnswer);
		AnswerGrade.belongsTo(models.Team, {
			as: 'gradingTeam',
			foreignKey: 'gradingTeamId'
		});
	};
	return AnswerGrade;
};