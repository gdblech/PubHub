'use strict';
module.exports = {
  up: (queryInterface, Sequelize) => {
    return queryInterface.changeColumn('TriviaGames', 'text', {type: Sequelize.STRING(1024)});
  },
  down: (queryInterface, Sequelize) => {
    return queryInterface.changeColumn('TriviaGames', 'text');
  }
};