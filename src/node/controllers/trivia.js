const Models = require('../models');
const imageStore = require('../utils/imageStore.js');
const log4js = require('log4js');
let logger = log4js.getLogger();
logger.level = process.env.LOG_LEVEL || 'info';


/**
 * Function for listing all availble TrivaiaGames
 * @param {*} req 
 * @param {*} res 
 * 
 */
let list = async (req, res) => {
	try {
		let triviagames = await Models.TriviaGame.findAll({
			order: [
				['createdAt', 'DESC'],
			],
		});
		res.status(200).send(triviagames)
	} catch (err) {
		res.status(400).send('Trivia List message error testing');
	}
}

/**
 * Function for retriving Trivia Game by Id
 * 
 * @param {*} req 
 * @param {*} res 
 */
let getById = async (req, res) => {
	try {
		let triviagame = await Models.TriviaGame
			.findById(req.params.id, {
				include: [{
					model: Models.TriviaRound,
					as: 'triviaRounds',
					include: [{
						model: Models.TriviaQuestion,
						as: 'triviaQuestions'
					}]
				}],
			});

		if (!triviagame) {
			res.status(404).send({
				messsage: 'Trivia Game not found',
			});
		} else {
			res.status(200).send(triviagame);
		}
	} catch (err) {
		logger.error(err);
		res.status(500).send(err);
	}
};

/**
 * Function for adding Trivia Game
 * @param {*} req 
 * @param {*} res 
 */
let add = async (req, res) => {
	let user = req.user;

	console.log('above');
	console.log(user.dataValues)
	console.log(user.userName)
	console.log('below');

	let triviaObj = req.body;
	//replacing the images with an id
	try {
		if (triviaObj.image) {
			triviaObj.image = await imageStore.put(triviaObj.image);
		}
		for (let i = 0; i < triviaObj.triviaRounds.length; i++) {
			let round = triviaObj.triviaRounds[i];
			if (round.image) {
				round.image = await imageStore.put(round.image);
			}
			for (let j = 0; j < round.triviaQuestions.length; j++) {
				let question = round.triviaQuestions[j];
				if (question.image) {
					question.image = await imageStore.put(question.image);
				}
			}
		}

	} catch (err) {
		logger.error(err);
		res.status(500).send(err);
	}

	try {

		let triviagame = await Models.TriviaGame.create(triviaObj, {
			include: [{
				model: Models.TriviaRound,
				as: 'triviaRounds',
				include: [{
					model: Models.TriviaQuestion,
					as: 'triviaQuestions'
				}]
			}]
		});

		await triviagame.setHost(user);

		res.status(200).send(triviagame);
	} catch (err) {
		logger.error(err);
		res.status(400).send(err);
	}

	// triviagame.hostName = "user.userName ";   

};

module.exports = {
	list,
	getById,
	add,
};