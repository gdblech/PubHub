const log4js = require('log4js');
let logger = log4js.getLogger();

let auth = async (req, res) => {
	const User = require('../models/user');
	const {
		OAuth2Client
	} = require('google-auth-library');
	const CLIENT_ID = process.env.GOOGLE_CLIENT_ID;
	logger.debug(CLIENT_ID);
	const client = new OAuth2Client(CLIENT_ID);

	logger.debug(req.token);
	let gtoken = req.token;
	try {
		const ticket = await client.verifyIdToken({
			idToken: gtoken,
			audience: CLIENT_ID
		});
		const payload = ticket.getPayload();
		const userid = payload['sub'];
	} catch (err) {
		logger.error(err);
	}


};

let validate = () => {

};

module.exports = {
	auth,
	validate
}