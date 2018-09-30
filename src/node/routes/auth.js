const express = require('express');
let router = express.Router();
let authController = require('../controllers/authentication.js');

router.get('/', authController.auth);

module.exports = router;