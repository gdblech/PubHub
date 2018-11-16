'use strict';
module.exports = {
	up: (queryInterface, Sequelize) => {
		return queryInterface.addColumn('Teams', 'teamLeaderId', Sequelize.INTEGER);
	},
	down: (queryInterface, Sequelize) => {
		return queryInterface.removeColumn('Teams', 'teamLeaderId');
	}
};