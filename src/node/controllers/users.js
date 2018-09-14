const db = require('./db_controller.js');
const log4js = require('log4js');
let logger = log4js.getLogger();
logger.level = process.env.LOG_LEVEL;

module.exports = {
	getUser: async (req, res) => {
		try {
			user = await db.getUser(req.params.id);
			logger.debug(JSON.stringify(user));
			res.status(200).send(user);
		} catch (err) {
			logger.error(err.message);
			res.status(500).send(err.message);
		}
	}
}