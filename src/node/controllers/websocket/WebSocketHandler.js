'use strict';
const Messages = require('./Messages');
const Models = require('../../models');
const log4js = require('log4js');
let logger = log4js.getLogger();
logger.level = process.env.LOG_LEVEL || 'info';

const http = require('http');
const ws = require('ws');


class WebSocketHandler {
	constructor(port, validator) {
		this.validate = validator;
		this.wss = new ws.Server({
			port,
			verifyClient: this.verifyClient.bind(this)
		});

		// bind is used to keep the class (this) in the scope of the connectHandler
		this.wss.on('connection', this.connectHandler.bind(this));

		this.interval = setInterval(this.ping.bind(this), 300000);
	}

	// event handler for connections
	connectHandler(client, info) {
		logger.debug('WS Connection');
		client.user = info.user;

		// base message handler needs to be defined inside connect handler to keep client in scope
		client.on('message', (data) => {
			let clientMessage;
			try {
				clientMessage = new Messages.WSClientMessage(data);
			} catch (err) {
				logger.error(`Error parsing websocket message: ${err}`);
				let response = {
					messageType: 'Error',
					error: err
				};
				client.send(JSON.stringify(response));
				return;
			}

			if (clientMessage.messageType === Messages.WSClientMessage.MESSAGE_TYPES.ClientServerChatMessage) {
				this.chatHandler(clientMessage.payload, client);
			} else {
				let response = {
					messageType: 'Error',
					error: 'Message type not handled'
				};
				client.send(JSON.stringify(response));
			}
		});

		client.on('close', () => {
			console.log(`User: ${client.user.userName} disconnected`);
		});

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
		let jwt = info.req.headers.authorization.replace('Bearer ', '');
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

	// event handler for ws messages
	async chatHandler(message, client) {
		var utc = new Date().toJSON();
		let chatMessage = await Models.ChatMessage.create({
			message: message.message,
			timestamp: utc
		});

		await chatMessage.setUser(client.user);
		let serverChatMessage = new Messages.ServerClientChatMessage(client.user.userName, chatMessage.message, chatMessage.timestamp);
		let outgoingMessage = JSON.stringify(serverChatMessage.toServerMessage());

		this.wss.clients.forEach((sclient) => {
			if (client !== sclient) {
				sclient.send(outgoingMessage);
			}
		});
	}
}

module.exports = WebSocketHandler;