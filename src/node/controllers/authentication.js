const log4js = require('log4js');
let logger = log4js.getLogger();

let auth = async (req, res) => {
	const User = require('../models').User;
	const Role = require('../models').Role;
	const AuthType = require('../models').AuthType;

	console.log(JSON.stringify(User));
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
		const userId = payload['sub'];
		const email = payload['email'];
		const userName = payload['email'];
		const profilePicPath = payload['picture'];
		const customerRole = await Role.find({
			where: {
				roleName: 'Customer'
			}
		});
		let googleAuth = await AuthType.find({
			where: {
				provider: 'google'
			}
		});

		logger.debug(`role: ${customerRole.id}`);
		let defaults = {
			userId,
			email,
			userName,
			profilePicPath
		}
		//logger.debug(JSON.stringify(defaults));
		let user = await User.findOrCreate({
			where: {
				userId
			},
			defaults,
		});

		logger.debug(`user: ${JSON.stringify(user)}`);
		res.send('done');
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