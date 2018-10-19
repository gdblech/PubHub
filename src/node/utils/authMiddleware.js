const authMiddleware = async function (req, res, next) {
	const log4js = require('log4js');
	let logger = log4js.getLogger();
	logger.level = process.env.LOG_LEVEL || 'info';

	// ignore requests for a token
	if (req.path === '/api/auth') {
		next();
		return;
	}
	const authentication = require('../controllers/authentication.js');

	try {
		let user = await authentication.validate(req.token);
		if (user) {
			req.user = user;
			next();
		} else {
			res.status(404).send("User profile not found");
		}

	} catch (err) {
		logger.error(err);
		if (err.type === "JWT-VALIDATION") {
			res.status(401).send(`Invalid token: ${err.message}`);
		} else if (err.type === "USER-LOAD") {
			// unexpected error condition
			res.status(500).send(`Error loading user: ${err.message}`);
		} else {
			// unexpected error condition
			res.status(500).send(`Error loading user: ${err.message}`);
		}
	}
}

module.exports = authMiddleware;