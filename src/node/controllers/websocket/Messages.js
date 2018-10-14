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
	constructor(payload) {
		this.message = payload.message;
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
		this.payload = message;
	}

	toJSON() {
		return {
			messageType: this.messageType,
			payload: this.payload
		};
	}
}

class ServerClientChatMessage {
	constructor(messages) {
		if (messages.length || messages.length === 0) {
			this.messages = messages;
		} else {
			this.messages = [messages];
		}
	}

	toServerMessage() {
		return new WSServerMessage(this);
	}

	toJSON() {
		return {
			messages: this.messages
		};
	}

}

module.exports = {
	WSClientMessage,
	WSServerMessage,
	ClientServerChatMessage,
	ServerClientChatMessage
}