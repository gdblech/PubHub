'use strict';
module.exports = {
  up: (queryInterface, Sequelize) => {
    return queryInterface.addColumn('TriviaGames', 'gameName', Sequelize.STRING);
  },
  down: (queryInterface, Sequelize) => {
    return queryInterface.removeColumn('TriviaGames', 'gameName');
  }
};