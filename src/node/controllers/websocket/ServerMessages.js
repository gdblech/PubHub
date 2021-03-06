/**
 * WSServerMessage
 * Main class for sending messages from server to clients. Contains an instance of
 * a supported message type.
 */
class WSServerMessage {
	/**
	 * MESSAGE_TYPES
	 * 'Enum' for supported message types
	 */
	static get MESSAGE_TYPES() {
		return {
			ServerClientChatMessage: 'ServerClientChatMessage',
			ServerHostMessage: 'ServerHostMessage',
			ServerPlayerMessage: 'ServerPlayerMessage'
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
			this.messageType = WSServerMessage.MESSAGE_TYPES.ServerClientChatMessage;
		} else if (message instanceof ServerHostMessage) {
			this.messageType = WSServerMessage.MESSAGE_TYPES.ServerHostMessage;
		} else if (message instanceof ServerPlayerMessage) {
			this.messageType = WSServerMessage.MESSAGE_TYPES.ServerPlayerMessage;
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
			payload: this.payload.toJSON()
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

/**
 * Class for messages from the server to the Host.
 */
class ServerHostMessage {
	/**
	 * Constructor for building the message.
	 * @param {*} messageType Message sub-type.
	 * @param {*} payload Payload contained in message as a JSON object.
	 */
	constructor(messageType, payload) {
		if (ServerHostMessage.MESSAGE_TYPES[messageType]) {
			this.messageType = messageType;
		} else {
			throw 'Invalid ServerHostMessage message type';
		}

		this.payload = payload;
	}

	static get MESSAGE_TYPES() {
		return {
			TriviaStart: 'TriviaStart',
			RoundStart: 'RoundStart',
			Question: 'Question',
			AnswerStatus: "AnswerStatus",
			Grading: 'Grading',
			GradingStatus: 'GradingStatus',
			// Scores: 'Scores',
			// Winners: 'Winners'
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
	 * Returns a the message as a JSON object.
	 */
	toJSON() {
		return {
			messageType: this.messageType,
			payload: this.payload
		};
	}
}

/**
 * Class for messages that the Server sends to players.
 */
class ServerPlayerMessage {
	/**
	 * Constructor that builds the message.
	 * @param {*} messageType Message sub-type.
	 * @param {*} payload Payload contained in the message as a JSON object.
	 */
	constructor(messageType, payload) {
		if (ServerPlayerMessage.MESSAGE_TYPES[messageType]) {
			this.messageType = messageType;
		} else {
			throw 'Invalid ServerPlayerMessage message type';
		}

		this.payload = payload;
	}

	static get MESSAGE_TYPES() {
		return {
			GameInfo: 'GameInfo',
			TableStatusResponse: 'TableStatusResponse',
			CreateTeamResponse: 'CreateTeamResponse',
			JoinTeamResponse: 'JoinTeamResponse',
			TriviaStart: 'TriviaStart',
			RoundStart: 'RoundStart',
			Question: 'Question',
			AnswerSubmission: 'AnswerSubmission',
			FinalAnswerResponse: 'FinalAnswerResponse',
			Grading: 'Grading',
			Scores: 'Scores',
			// Winners: 'Winners'
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
	 * Returns a the message as a JSON object.
	 */
	toJSON() {
		return {
			messageType: this.messageType,
			payload: this.payload
		};
	}
}

module.exports = {
	WSServerMessage,
	ServerClientChatMessage,
	ServerPlayerMessage,
	ServerHostMessage
}