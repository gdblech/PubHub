'use strict';
let Models = require('../models');

module.exports = {
	up: async (queryInterface, Sequelize) => {
		let admin = await Models.Role.create({
			roleName: 'Admin',
			createdAt: '2018-09-21',
			updatedAt: '2018-09-21'
		});

		let employee = await Models.Role.create({
			roleName: 'Employee',
			createdAt: '2018-09-21',
			updatedAt: '2018-09-21'
		});

		let host = await Models.Role.create({
			roleName: 'Host',
			createdAt: '2018-09-21',
			updatedAt: '2018-09-21'
		});

		let customer = await Models.Role.create({
			roleName: 'Customer',
			createdAt: '2018-09-21',
			updatedAt: '2018-09-21'
		});

		let google = await Models.AuthType.create({
			provider: 'google',
			createdAt: '2018-09-21',
			updatedAt: '2018-09-21'
		});

		let user1 = await Models.User.create({
			userId: '118380142220317436302',
			userName: 'tlcox3',
			email: 'tlcox3@uncg.edu',
			profilePicPath: 'https://lh6.googleusercontent.com/-c3RQ0oSk7e4/AAAAAAAAAAI/AAAAAAAAAXQ/AUEePV-pDT8/s96-c/photo.jpg',
			createdAt: '2018-09-21',
			updatedAt: '2018-09-21'
		});

		await user1.setRole(admin);
		await user1.setAuthType(google);

		let user2 = await Models.User.create({
			userId: '111590387447879681811',
			userName: 'tcox3799',
			email: 'tcox3799@gmail.com',
			profilePicPath: 'https://lh3.googleusercontent.com/-eq3I1UzfXw0/AAAAAAAAAAI/AAAAAAAAAAA/AAN31DU5WM3_WIVWRbJPHXDcll21fVQQ3A/s96-c/photo.jpg',
			createdAt: '2018-09-21',
			updatedAt: '2018-09-21'
		});

		await user2.setRole(host);
		await user2.setAuthType(google);

		let user3 = await Models.User.create({
			userId: '104463408769641250676',
			userName: 'keshek',
			email: 'keshek@gmail.com',
			profilePicPath: 'https://lh6.googleusercontent.com/-3f9mQvb4mSc/AAAAAAAAAAI/AAAAAAAAAAA/2wwXfl8lcgA/s96-c/photo.jpg',
			createdAt: '2018-09-21',
			updatedAt: '2018-09-21'
		});

		await user3.setRole(customer);
		await user3.setAuthType(google);


		// Tables
		let tables = ['1539c834-e391-11e8-9f32-f2801f1b9fd1',
			'1539c988-e391-11e8-9f32-f2801f1b9fd1',
			'1539cac8-e391-11e8-9f32-f2801f1b9fd1',
			'1539ccd0-e391-11e8-9f32-f2801f1b9fd1',
			'1539d22a-e391-11e8-9f32-f2801f1b9fd1',
			'1539d39c-e391-11e8-9f32-f2801f1b9fd1',
			'1539d4c8-e391-11e8-9f32-f2801f1b9fd1',
			'1539d5ea-e391-11e8-9f32-f2801f1b9fd1',
			'1539d70c-e391-11e8-9f32-f2801f1b9fd1'
		];

		for (let i = 0; i < tables.length; i++) {
			await Models.Table.create({
				qrCode: tables[i]
			});
		}

		let triviaGame = {
			"date": "2018-12-25T05:00:00.000Z",
			"hostName": "Trivia Master",
			"gameName": "ChampTrivia",
			"title": "Ultimate Trivia of Champions",
			"text": "The Greatest Trivia Ever",
			"image": "45f1d4e0-e6f6-11e8-aca3-ffe90892d3f6",
			"completed": false,
			"triviaRounds": [{
					"roundNumber": 2,
					"title": "Movies",
					"text": "Name the movie in the image",
					"image": "471c9940-e6f6-11e8-aca3-ffe90892d3f6",
					"triviaQuestions": [{
							"questionNumber": 2,
							"title": "Sci-Fi",
							"text": "Released 1996",
							"image": "4775dcd0-e6f6-11e8-aca3-ffe90892d3f6",
							"answer": "Independence Day"
						},
						{
							"questionNumber": 1,
							"title": "Drama",
							"text": "Released 2014",
							"image": "47a8d3b0-e6f6-11e8-aca3-ffe90892d3f6",
							"answer": "Imitation Game"
						}
					]
				},
				{
					"roundNumber": 1,
					"title": "Weird Al",
					"text": "Name the original song that Weird Al Parodied",
					"image": "47f93da0-e6f6-11e8-aca3-ffe90892d3f6",
					"triviaQuestions": [{
							"questionNumber": 1,
							"title": "Smells Like Nirvana",
							"text": "Released 1992",
							"image": "4905a490-e6f6-11e8-aca3-ffe90892d3f6",
							"answer": "Smells Like Teen Spirit"
						},
						{
							"questionNumber": 2,
							"title": "Amish Paradise",
							"text": "Released 1996",
							"image": "498a16d0-e6f6-11e8-aca3-ffe90892d3f6",
							"answer": "Gangsta's Paradise"
						}
					]
				}
			]
		};



		let trivia = await Models.TriviaGame.create(triviaGame, {
			include: [{
				model: Models.TriviaRound,
				as: 'triviaRounds',
				include: [{
					model: Models.TriviaQuestion,
					as: 'triviaQuestions'
				}]
			}]
		});

		trivia.setHost(user2);
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