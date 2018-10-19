const TriviaGame = require('../models').TriviaGame;
const TriviaRound = require('../models').TriviaRound;
const Team = require('../models').Team;

/**
 * Function for listing all availble TrivaiaGames
 * @param {*} req 
 * @param {*} res 
 * 
 */
let list = async(req, res) =>{
    return TriviaGame
    .findAll({
      include: [{
        model: TriviaRound,
        as: 'triviaround'
      },{
        model: Team,
        as: 'team'
      }],
      order: [
        ['createdAt', 'DESC'],
        [{ model: TriviaRound, as: 'triviaround' }, 'createdAt', 'DESC'],
      ],
    })
    .then((triviagames) => res.status(200).send(triviagames))
    .catch((error) => { res.status(400).send(error); });
}

/**
 * Function for retriving Trivia Game by Id
 * 
 * @param {*} req 
 * @param {*} res 
 */
let getById = async(req,res) =>{
    return TriviaGame
        .findById(req.params.id, {
            include: [{
                model: TriviaRound,
                as: 'triviaround'
            },{
                model: Team,
                as: 'team'
            }],
        })
        .then((TriviaGame) => {
            if(!triviagame){
                return res.status(404).send({
                    messsage: 'Trivia Game not found',
                });
            }
            return res.status(200).send(triviagame);
        })
        .catch((error) => res.status(400).send(error));
};


let add = async(req, res) =>{
    return TriviaGame
      .create({
        id: req.body.id,
        //more attributes
        
      })
      .then((triviagame) => res.status(201).send(triviagame))
      .catch((error) => res.status(400).send(error));

};




module.exports = {
	list,
    getById,
    add
};



