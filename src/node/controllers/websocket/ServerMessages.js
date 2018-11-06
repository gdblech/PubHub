/**
 * WSServerMessage
 * Main class for sending messages from server to clients. Contains an instance of
 * a supported message type.
 */
class WSServerMessage {
	/**
	 * MESSAGE_TYPES
	 * "Enum" for supported message types
	 */
	static get MESSAGE_TYPES() {
		return {
			ClientServerChatMessage: 'ServerClientChatMessage'
		}
	}

	/**
	 * constructor
	 * Builds a WSServerMessage from an instance of a supported message type. Throws
	 * exception if message is not an instance of a supported message type.
	 * @param {*} message: An instance of a supported message type, set as payload.
	 */
	constructor(message) {
		if (message instanceof ServerClientChatMessage) {
			this.messageType = 'ServerClientChatMessage';
		} else {
			throw 'Invalid message type'
		}
		this.payload = message;
	}

	/**
	 * toJSON
	 * Returns a JSON object representation of the WSServerMessage.
	 */
	toJSON() {
		return {
			messageType: this.messageType,
			payload: this.payload
		};
	}
}

/**
 * ServerClientChatMessage
 * Class for handling chat messages the server sends to clients.
 */
class ServerClientChatMessage {
	constructor(messages) {
		if (messages.length || messages.length === 0) {
			this.messages = messages;
		} else {
			this.messages = [messages];
		}
	}

	/**
	 * toServerMessage
	 * Returns the message wrapped in a WSServerMessage
	 */
	toServerMessage() {
		return new WSServerMessage(this);
	}

	/**
	 * toJSON
	 * Returns a JSON object representation of the ServerClientChatMessage.
	 */
	toJSON() {
		return {
			messages: this.messages
		};
	}

}

module.exports = {
	WSServerMessage,
	ServerClientChatMessage
}