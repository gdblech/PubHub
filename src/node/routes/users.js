const express = require('express');
let router = express.Router();
let userController = require('../controllers/users.js');

router.get('/:id', userController.getUser);

module.exports = router;