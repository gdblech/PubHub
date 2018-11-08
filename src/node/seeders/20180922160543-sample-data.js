'use strict';
let Models = require('../models')
let User = Models.User;
let Role = Models.Role;
let AuthType = Models.AuthType;

module.exports = {
	up: async (queryInterface, Sequelize) => {
		let admin = await Role.create({
			roleName: 'Admin',
			createdAt: '2018-09-21',
			updatedAt: '2018-09-21'
		});

		let employee = await Role.create({
			roleName: 'Employee',
			createdAt: '2018-09-21',
			updatedAt: '2018-09-21'
		});

		let host = await Role.create({
			roleName: 'Host',
			createdAt: '2018-09-21',
			updatedAt: '2018-09-21'
		});

		let customer = await Role.create({
			roleName: 'Customer',
			createdAt: '2018-09-21',
			updatedAt: '2018-09-21'
		});

		let google = await AuthType.create({
			provider: 'google',
			createdAt: '2018-09-21',
			updatedAt: '2018-09-21'
		})

		let user = await User.create({
			userId: '118380142220317436302',
			userName: 'tlcox3',
			email: 'tlcox3@uncg.edu',
			roleId: 1,
			authTypeId: 1,
			createdAt: '2018-09-21',
			updatedAt: '2018-09-21'
		})

		await user.setRole(admin);
		await user.setAuthType(google);
	},

	down: (queryInterface, Sequelize) => {
		/*
		  Add reverting commands here.
		  Return a promise to correctly handle asynchronicity.

		  Example:
		  return queryInterface.bulkDelete('Person', null, {});
		*/
	}
};