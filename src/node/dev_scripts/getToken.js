const jwt = require('jsonwebtoken');
const User = require('../models').User;
const Role = require('../models').Role;

let getToken = async (email) => {
	try {
		let user = await User.find({
			where: {
				email
			},
			include: [Role]
		});

		// Generate pubhub JWT and send as response
		let payload = user.getJWTPayload();
		if (process.env.JWT_KEY) {
			let token = jwt.sign(payload, process.env.JWT_KEY, {
				expiresIn: '6h'
			});
			console.log(`Token: ${token}`);
		} else {
			console.log('JWT private key not set');
		}
		process.exit();
	} catch (err) {
		console.log(err);
		process.exit();
	}
}

let email = process.argv[2];

getToken(email);