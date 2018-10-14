class WSClientMessage {
	constructor(json) {
		let parsed;
		try {
			parsed = JSON.parse(json);
		} catch (err) {
			throw 'Message not in JSON format';
		}

		this.messageType = parsed.messageType;
		if (this.messageType === WSClientMessage.MESSAGE_TYPES.ClientServerChatMessage) {
			this.payload = new ClientServerChatMessage(parsed.payload);
		} else {
			throw 'Invalid message type';
		}
	}

	static get MESSAGE_TYPES() {
		return {
			ClientServerChatMessage: 'ClientServerChatMessage'
		}
	}

	toObject() {
		let obj = {
			messageType: this.messageType,
			payload: this.payload.toJSONObject()
		}
		return obj;
	}

	toJSON() {
		return JSON.stringify(this.toObject());
	}
}

class ClientServerChatMessage {
	constructor(message) {
		this.message = message;
	}

	toObject() {
		let json = {
			message: this.message
		}
		return json;
	}

	toJSON() {
		return JSON.stringify(this.toObject());
	}
}

class WSServerMessage {
	static get MESSAGE_TYPES() {
		return {
			ClientServerChatMessage: 'ServerClientChatMessage'
		}
	}

	constructor(message) {
		if (message instanceof ServerClientChatMessage) {
			this.messageType = 'ServerClientChatMessage';
		} else {
			throw 'Invalid message type'
		}

	}
}

class ServerClientChatMessage {
	constructor(sender, message) {
		this.sender = sender;
		this.message = message;
	}

	toServerMessage() {
		return new WSServerMessage('ServerClientChatMessage', this);
	}

	toObject() {
		return {

		}
	}

}

module.exports = {
	WSClientMessage,
	WSServerMessage,
	ClientServerChatMessage,
	ServerClientChatMessage
}