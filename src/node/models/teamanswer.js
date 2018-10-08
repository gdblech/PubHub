'use strict';
module.exports = (sequelize, DataTypes) => {
  const TeamAnswer = sequelize.define('TeamAnswer', {
    answer: DataTypes.STRING,
    correct: DataTypes.BOOLEAN
  }, {});
  TeamAnswer.associate = function(models) {
    // associations can be defined here
    // TeamAnswer.belongsTo(models.Team);
    // TeamAnswer.belongsTo(models.TriviaQuestion);
    TeamAnswer.hasMany(models.AnswerGrade);
  };
  return TeamAnswer;
};