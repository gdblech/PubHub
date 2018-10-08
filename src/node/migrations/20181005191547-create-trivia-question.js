'use strict';
module.exports = {
  up: (queryInterface, Sequelize) => {
    return queryInterface.createTable('TriviaQuestions', {
      id: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      triviaRoundId: {
        type: Sequelize.INTEGER
      },
      questionNumber: {
        type: Sequelize.INTEGER
      },
      title: {
        type: Sequelize.STRING
      },
      answer: {
        type: Sequelize.STRING
      },
      text: {
        type: Sequelize.STRING
      },
      image: {
        type: Sequelize.STRING
      },
      createdAt: {
        allowNull: false,
        type: Sequelize.DATE
      },
      updatedAt: {
        allowNull: false,
        type: Sequelize.DATE
      }
    });
  },
  down: (queryInterface, Sequelize) => {
    return queryInterface.dropTable('TriviaQuestions');
  }
};