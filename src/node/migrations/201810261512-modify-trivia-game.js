'use strict';
module.exports = {
	up: (queryInterface, Sequelize) => {
		return queryInterface.addColumn('TriviaGames', 'completed', Sequelize.BOOLEAN);
	},
	down: (queryInterface, Sequelize) => {
		return queryInterface.removeColumn('TriviaGames', 'completed');
	}
};