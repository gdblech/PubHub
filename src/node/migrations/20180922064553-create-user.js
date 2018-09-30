'use strict';
module.exports = {
	up: (queryInterface, Sequelize) => {
		return queryInterface.createTable('Users', {
			id: {
				allowNull: false,
				autoIncrement: true,
				primaryKey: true,
				type: Sequelize.INTEGER
			},
			userId: {
				type: Sequelize.STRING,
				allowNull: false,
				unique: true
			},
			userName: {
				type: Sequelize.STRING,
				allowNull: false,
				unique: true
			},
			email: {
				type: Sequelize.STRING,
				allowNull: false,
				unique: true
			},
			profilePicPath: {
				type: Sequelize.STRING
			},
			createdAt: {
				allowNull: false,
				type: Sequelize.DATE
			},
			updatedAt: {
				allowNull: false,
				type: Sequelize.DATE
			},
			roleId: {
				type: Sequelize.INTEGER,
				references: {
					model: 'Roles',
					key: 'id'
				}
			},
			authTypeId: {
				type: Sequelize.INTEGER,
				references: {
					model: 'AuthTypes',
					key: 'id'
				}
			}
		});
	},
	down: (queryInterface, Sequelize) => {
		return queryInterface.dropTable('Users');
	}
};