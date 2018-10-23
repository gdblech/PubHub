const Models = require('../models');
const TriviaGame = require('../models').TriviaGame;
const TriviaRound = require('../models').TriviaRound;
const Team = require('../models').Team;
const log4js = require('log4js');
let logger = log4js.getLogger();
logger.level = process.env.LOG_LEVEL || 'info';

/**
 * Function for listing all availble TrivaiaGames
 * @param {*} req 
 * @param {*} res 
 * 
 */
let list = async(req, res) =>{
    try{
        let triviagames = await TriviaGame.findAll({
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
let getById = async(req,res) =>{
    try {
        let triviagame = await TriviaGame
            .findById(req.params.id, {
                include: [{
                    model: TriviaRound
                }],
            });

        if(!triviagame){
            res.status(404).send({
                messsage: 'Trivia Game not found',
            });
        } else {
            res.status(200).send(triviagame);
        }
    } catch (err) {
        res.status(500).send(error);
    }
};

/**
 * Function for adding Trivia Game
 * @param {*} req 
 * @param {*} res 
 */
let add = async(req, res) =>{
    let user = req.user;
    
    console.log('above');
    console.log(user.dataValues)
    console.log(user.userName)
    console.log('below');

    //logger.debug('start');
    try {
        
        let triviagame = await TriviaGame.create(req.body, {
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