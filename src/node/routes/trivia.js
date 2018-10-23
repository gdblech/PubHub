const express = require('express');
let router = express.Router();
let triviaGameController  = require('../controllers/trivia.js');

router.get('/', triviaGameController.list);

router.get('/:id', triviaGameController.getById);

router.post('/', triviaGameController.add);

module.exports = router;