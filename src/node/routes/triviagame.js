const express = require('express');
let router = express.Router();
//let authController = require('../controllers/authentication.js');
let triviaGameController  = require('../controllers/triviagame.js');

router.use('/triagame', triviaGameController);

module.exports = router;