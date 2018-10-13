'use strict';

class ClientManager {
	constructor() {
		this.clients = [];
	}

	removeClient(userId) {
		delete this.clients[userId];
	}

	addClient(userId, client) {
		this.clients[userId] = client;
	}

	getClient(userId) {
		return this.clients[userId];
	}

	forEach(cb) {
		Object.keys(this.clients).forEach(key => cb(this.clients[key]));
	}
}

module.exports = ClientManager;