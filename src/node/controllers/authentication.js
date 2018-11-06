const log4js = require('log4js');
let logger = log4js.getLogger();
logger.level = process.env.LOG_LEVEL || 'info';

const DEFAULT_ROLE = 'Customer';
const DEFAULT_PROVIDER = 'google';
const jwt = require('jsonwebtoken');
const User = require('../models').User;
const Role = require('../models').Role;

/**
 * Function for handling a authorization request, validates the
 * Google sign in JWT, and sends a PubHub generated JWT
 * @param {*} req 
 * @param {*} res 
 */
let auth = async (req, res) => {
	// configure Google OAuth2Client
	const {
		OAuth2Client
	} = require('google-auth-library');
	const CLIENT_ID = process.env.GOOGLE_CLIENT_ID;
	logger.debug(CLIENT_ID);
	const client = new OAuth2Client(CLIENT_ID);

	let gtoken = req.token;
	let ticket;

	// validate token and get id from it
	try {
		ticket = await client.verifyIdToken({
			idToken: gtoken,
			audience: CLIENT_ID
		});
	} catch (err) {
		logger.error(err);
		res.status(401).send('Invalid token: ' + err);
		return;
	}

	const payload = ticket.getPayload();
	const userId = payload['sub'];
	const email = payload['email'];
	const userName = payload['email'];
	const profilePicPath = payload['picture'];

	let defaults = {
		userId,
		email,
		userName,
		profilePicPath,
		roleName: DEFAULT_ROLE,
		authType: DEFAULT_PROVIDER
	}

	// Load the user if they exist, create a new user if not, using information from JWT
	try {
		let user = await User.findOrCreate2({
			where: {
				userId
			},
			defaults,
		});

		// Generate pubhub JWT and send as response
		logger.debug(`user: ${JSON.stringify(user)}`);
		let payload = user.getJWTPayload();
		if (process.env.JWT_KEY) {
			let token = jwt.sign(payload, process.env.JWT_KEY, {
				expiresIn: '6h'
			});
			res.send(token);
		} else {
			logger.error('JWT private key not set');
			res.status(500).send('Server error');
		}

	} catch (err) {
		logger.error(err);
		res.status(500).send('Error loading user');
	}

};

/**
 * Function for validating a PubHub JWT, returns the user from the JWT
 * @param {*} token 
 */
let validate = async (token) => {
	let decoded;
	try {
		decoded = jwt.verify(token, process.env.JWT_KEY);
	} catch (err) {
		err.type = "JWT-VALIDATION";
		throw err;
	}

	let user;
	try {
		user = await User.find({
			where: {
				userId: decoded.userId
			},
			include: [Role]
		});
	} catch (err) {
		err.type = "USER-LOAD";
		throw err;
	}
	return user;
};

/**
 * Function for responding to a profile request, gets userId from PubHub JWT
 * @param {*} req 
 * @param {*} res 
 */
let getProfile = async (req, res) => {
	res.send(JSON.stringify(req.user));
}

module.exports = {
	auth,
	validate,
	getProfile
}