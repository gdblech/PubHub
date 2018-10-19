const TriviaRound = require('../models').TriviaRound;
const TriviaGame = require('../models').TriviaGame;
const TriviaQuestion = require('../models').TriviaQuestion;



/**
 * Function for listing all availble TriviaRound
 * @param {*} req 
 * @param {*} res 
 * 
 */
  let  list = async(req, res) => {
      return TriviaRound
        .findAll({
          include: [{
            model: TriviaGame,
            as: 'triviagame'
          },{
            model: TriviaQuestion,
            as: 'triviaquestion'
          }],
          order: [
            ['createdAt', 'DESC'],
            [{ model: TriviaQuestion, as: 'triviaquestion' }, 'createdAt', 'DESC'],
          ],
        })
        .then((triviarounds) => res.status(200).send(triviarounds))
        .catch((error) => { res.status(400).send(error); });
    };
  
  let getById = async(req, res) => {
      return TriviaRound
        .findById(req.params.id, {
          include: [{
            model: TriviaGame,
            as: 'triviagame'
          },{
            model: TriviaQuestion,
            as: 'triviaquestion'
          }],
        })
        .then((triviaround) => {
          if (!triviaround) {
            return res.status(404).send({
              message: 'trivia round Not Found',
            });
          }
          return res.status(200).send(triviaround);
        })
        .catch((error) => res.status(400).send(error));
    };
  
  let add = async(req, res) => {
      return TriviaRound
        .create({
          id: req.body.id,
          //more attribute
        })
        .then((triviaround) => res.status(201).send(triviaround))
        .catch((error) => res.status(400).send(error));
    };
  
  let update = async(req, res) => {
      return TriviaRound
        .findById(req.params.id, {
          include: [{
            model: TriviaGame,
            as: 'triviagame'
          },{
            model: TriviaQuestion,
            as: 'triviaquestion'
          }],
        })
        .then(triviaround => {
          if (!triviaround) {
            return res.status(404).send({
              message: 'trivia round Not Found',
            });
          }
          return triviaround
            .update({
              //student_name: req.body.student_name || classroom.student_name,
            })
            .then(() => res.status(200).send(triviaround))
            .catch((error) => res.status(400).send(error));
        })
        .catch((error) => res.status(400).send(error));
    };

    module.exports = {
      list,
      getById,
      add,
      update

    };