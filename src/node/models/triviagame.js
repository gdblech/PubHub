'use strict';
module.exports = (sequelize, DataTypes) => {
	const TriviaGame = sequelize.define('TriviaGame', {
		date: DataTypes.DATE,
		hostName: DataTypes.STRING,
		gameName: DataTypes.STRING,
		title: DataTypes.STRING,
		text: DataTypes.STRING,
		image: DataTypes.STRING,
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
		json.image = this.image;
		json.completed = this.completed;

		if (this.triviaRounds) {
			let rounds = [];
			for (let i = 0; i < this.triviaRounds.length; i++) {
				rounds.push(this.triviaRounds[i].toJSON());
			}
			json.triviaRounds = rounds;
		}

		return json;
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

		if (triviaGame.image) {
			triviaGame.image = await imageStore.get(triviaGame.image);
		}

		for (let i = 0; i < triviaGame.triviaRounds.length; i++) {
			if (triviaGame.triviaRounds[i].image) {
				triviaGame.triviaRounds[i].image = await imageStore.get(triviaGame.triviaRounds[i].image);
			}

			for (let j = 0; j < triviaGame.triviaRounds[i].triviaQuestions.length; j++) {
				if (triviaGame.triviaRounds[i].triviaQuestions[j].image) {
					triviaGame.triviaRounds[i].triviaQuestions[j].image = await imageStore.get(triviaGame.triviaRounds[i].triviaQuestions[j].image);
				}
			}
		}

		return triviaGame;
	}

	return TriviaGame;
};