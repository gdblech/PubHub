'use strict';
module.exports = {
	up: (queryInterface, Sequelize) => {
		return queryInterface.createTable('TriviaRounds', {
			id: {
				allowNull: false,
				autoIncrement: true,
				primaryKey: true,
				type: Sequelize.INTEGER
			},
			triviaGameId: {
				type: Sequelize.INTEGER
			},
			roundNumber: {
				type: Sequelize.INTEGER
			},
			title: {
				type: Sequelize.STRING
			},
			text: {
				type: Sequelize.STRING
			},
			imageId: {
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
		return queryInterface.dropTable('TriviaRounds');
	}
};