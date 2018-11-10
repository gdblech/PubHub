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
		});

		let user1 = await User.create({
			userId: '118380142220317436302',
			userName: 'tlcox3',
			email: 'tlcox3@uncg.edu',
			profilePicPath: 'https://lh6.googleusercontent.com/-c3RQ0oSk7e4/AAAAAAAAAAI/AAAAAAAAAXQ/AUEePV-pDT8/s96-c/photo.jpg',
			createdAt: '2018-09-21',
			updatedAt: '2018-09-21'
		});

		await user1.setRole(admin);
		await user1.setAuthType(google);

		let user2 = await User.create({
			userId: '111590387447879681811',
			userName: 'tcox3799',
			email: 'tcox3799@gmail.com',
			profilePicPath: 'https://lh3.googleusercontent.com/-eq3I1UzfXw0/AAAAAAAAAAI/AAAAAAAAAAA/AAN31DU5WM3_WIVWRbJPHXDcll21fVQQ3A/s96-c/photo.jpg',
			createdAt: '2018-09-21',
			updatedAt: '2018-09-21'
		});

		await user2.setRole(host);
		await user2.setAuthType(google);

		let user3 = await User.create({
			userId: '104463408769641250676',
			userName: 'keshek',
			email: 'keshek@gmail.com',
			profilePicPath: 'https://lh6.googleusercontent.com/-3f9mQvb4mSc/AAAAAAAAAAI/AAAAAAAAAAA/2wwXfl8lcgA/s96-c/photo.jpg',
			createdAt: '2018-09-21',
			updatedAt: '2018-09-21'
		});

		await user3.setRole(customer);
		await user3.setAuthType(google);
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