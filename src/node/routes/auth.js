const express = require('express');
let router = express.Router();
let authController = require('../controllers/authentication.js');

router.get('/', authController.auth);

router.get('/profile', authController.getProfile);

module.exports = router;