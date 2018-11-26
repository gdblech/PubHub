'use strict';
module.exports = (sequelize, DataTypes) => {
	const TriviaGame = sequelize.define('TriviaGame', {
		date: DataTypes.DATE,
		hostName: DataTypes.STRING,
		gameName: DataTypes.STRING,
		title: DataTypes.STRING,
		text: DataTypes.STRING,
		imageId: DataTypes.STRING,
		completed: DataTypes.BOOLEAN
	}, {});
	TriviaGame.associate = function (models) {
		// associations can be defined here
		TriviaGame.belongsTo(models.User, {
			as: 'host'
		});
		TriviaGame.hasMany(models.TriviaRound, {
			as: 'triviaRounds'
		});
		TriviaGame.hasMany(models.Team);
	};

	TriviaGame.prototype.toJSON = function () {
		let json = {};
		json.id = this.id;
		json.date = this.date;
		json.hostName = this.hostName;
		json.gameName = this.gameName;
		json.title = this.title;
		json.text = this.text;
		json.imageId = this.imageID;
		json.completed = this.completed;

		if (this.image) {
			json.image = this.image;
		}

		if (this.triviaRounds) {
			let rounds = [];
			for (let i = 0; i < this.triviaRounds.length; i++) {
				rounds.push(this.triviaRounds[i].toJSON());
			}
			json.triviaRounds = rounds;
		}

		return json;
	};

	TriviaGame.prototype.loadImages = async function () {
		const imageStore = require('../utils/imageStore');

		if (this.imageId) {
			this.image = await imageStore.get(this.imageId);
		}
		if (this.triviaRounds) {
			for (let i = 0; i < this.triviaRounds.length; i++) {
				await this.triviaRounds[i].loadImages();
			}
		}
	};

	TriviaGame.findWithImages = async function (id) {
		const Models = require('./');
		const imageStore = require('../utils/imageStore');
		let triviaGame = await Models.TriviaGame
			.findById(id, {
				order: [
					[{
						model: Models.TriviaRound,
						as: 'triviaRounds'
					}, 'roundNumber', 'ASC'],
					[{
							model: Models.TriviaRound,
							as: 'triviaRounds'
						},
						{
							model: Models.TriviaQuestion,
							as: 'triviaQuestions'
						}, 'questionNumber', 'ASC'
					]
				],
				include: [{
					model: Models.TriviaRound,
					as: 'triviaRounds',
					include: [{
						model: Models.TriviaQuestion,
						as: 'triviaQuestions'
					}]
				}],
			});
		if (!triviaGame) {
			return triviaGame;
		}

		await triviaGame.loadImages();

		// if (triviaGame.image) {
		// 	triviaGame.image = await imageStore.get(triviaGame.image);
		// }

		// for (let i = 0; i < triviaGame.triviaRounds.length; i++) {
		// 	if (triviaGame.triviaRounds[i].image) {
		// 		triviaGame.triviaRounds[i].image = await imageStore.get(triviaGame.triviaRounds[i].image);
		// 	}

		// 	for (let j = 0; j < triviaGame.triviaRounds[i].triviaQuestions.length; j++) {
		// 		if (triviaGame.triviaRounds[i].triviaQuestions[j].image) {
		// 			triviaGame.triviaRounds[i].triviaQuestions[j].image = await imageStore.get(triviaGame.triviaRounds[i].triviaQuestions[j].image);
		// 		}
		// 	}
		// }

		return triviaGame;
	}

	return TriviaGame;
};