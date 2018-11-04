/**
 * WSClientMessage
 * Class for processing incoming messages from clients. Will parse messages into
 * specific message types that are stored as payload.
 */
class WSClientMessage {
	/**
	 * constructor
	 * Constructor for a client to server message throws exception if message
	 * cannot be parsed or if message type is not implemented.
	 * @param {*} jsonString string of json containing a message
	 */
	constructor(jsonString) {
		let parsed;
		try {
			parsed = JSON.parse(jsonString);
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

	/** 
	 * MESSAGE_TYPES
	 * "Enum" for accepted message types.
	 */
	static get MESSAGE_TYPES() {
		return {
			ClientServerChatMessage: 'ClientServerChatMessage'
		}
	}

	/**
	 * toJSON
	 * Returns the message in a JSON object format.
	 */
	toJSON() {
		let obj = {
			messageType: this.messageType,
			payload: this.payload.toJSONObject()
		}
		return obj;
	}
}

/**
 * ClientServerChatMessage
 * Class for handling chat messages from clients. Payload should contain the message.
 */
class ClientServerChatMessage {
	/**
	 * constructor
	 * Builds the ClientServerChatMessage object, payload should be a parsed JSON
	 * object that has a message property.
	 * @param {*} payload: the message 
	 */
	constructor(payload) {
		this.message = payload.message;
	}

	/**
	 * toJSON
	 * Returns a JSON object representation of the ClientServerChatMessage.
	 */
	toJSON() {
		let json = {
			message: this.message
		}
		return json;
	}
}

module.exports = {
	WSClientMessage,
	ClientServerChatMessage
}