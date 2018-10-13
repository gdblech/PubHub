'use strict';
const log4js = require('log4js');
let logger = log4js.getLogger();
logger.level = process.env.LOG_LEVEL || 'info';

const ClientManager = require('./ClientManager');
const http = require('http');
const ws = require('ws');
class WebSocketHandler {

	// event handler for ws messages
	messageHandler(data) {
		console.log(data);
	}

	// event handler for connections
	connectHandler(client, info) {
		logger.debug('WS Connection');
		this.clients.addClient(info.user.userId, client);
		client.user = info.user;
		client.on('message', this.messageHandler.bind(this));

		client.isAlive = true;
		client.on('pong', this.heartbeat);
	}

	// event handler for checking broken connections connections
	heartbeat() {
		this.isAlive = true;
	}

	// ping client to check if connection is broken
	ping() {
		this.wss.clients.forEach((client) => {
			if (client.isAlive === false) {
				return client.terminate();
			}
			client.isAlive = false;
			client.ping(() => {});
		})
	}

	disconnectUser(userId) {
		this.wss.clients.forEach((client) => {
			if (client.user.userId === userId) {
				client.terminate();
			}
		});
	}

	async verifyClient(info, cb) {
		var jwt = info.req.headers.authorization.replace('Bearer ', '');
		try {
			let user = await this.validate(jwt);
			info.req.user = user;
			this.disconnectUser(user.userId);
			cb(true);
		} catch (err) {
			if (err.type == 'JWT-VALIDATION') {
				cb(false, 401, 'JWT Validation failed');
			} else if (err.type == 'USER-LOAD') {
				cb(false, 404, 'User not found');
			} else {
				logger.error(err);
				cb(false, 500, 'Unexpected error.');
			}
		}
	}



	constructor(port, validator) {
		this.validate = validator;
		this.clients = new ClientManager();
		this.wss = new ws.Server({
			port,
			verifyClient: this.verifyClient.bind(this)
		});

		// bind is used to keep the class (this) in the scope of the connectHandler
		this.wss.on('connection', this.connectHandler.bind(this));

		this.interval = setInterval(this.ping.bind(this), 3000);
	}


}

module.exports = WebSocketHandler;