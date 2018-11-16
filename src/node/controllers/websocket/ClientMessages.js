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
		} else if (this.messageType === WSClientMessage.MESSAGE_TYPES.PlayerServerMessage) {
			this.payload = new PlayerServerMessage(parsed.payload);
		} else if (this.messageType === WSClientMessage.MESSAGE_TYPES.HostServerMessage) {
			this.payload = new HostServerMessage(parsed.payload);
		} else {
			throw 'Invalid message WSClientMessage type';
		}
	}

	/** 
	 * MESSAGE_TYPES
	 * 'Enum' for accepted message types.
	 */
	static get MESSAGE_TYPES() {
		return {
			ClientServerChatMessage: 'ClientServerChatMessage',
			PlayerServerMessage: 'PlayerServerMessage',
			HostServerMessage: 'HostServerMessage'
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

class HostServerMessage {
	constructor(payload) {
		this.messageType = payload.messageType;
		if (!HostServerMessage.MESSAGE_TYPES[this.messageType]) {
			throw 'Invalid HostServerMessage message type';
		}
		this.payload = payload.payload;
	}

	static get MESSAGE_TYPES() {
		return {
			OpenGame: 'OpenGame',
			EndGame: 'EndGame',
			StartTrivia: 'StartTrivia',
			StartRound: 'StartRound',
			StartQuestion: 'StartQuestion',
			AnswerQuestion: 'AnswerQuestion',
			FinishRound: 'FinishRound',
			FinalGrade: 'FinalGrade',
			RoundScore: 'RoundScore',
			GameScore: 'GameScore'
		}
	}

	/**
	 * toJSON
	 * Returns a JSON object representation of the HostServerMessage.
	 */
	toJSON() {
		let json = {
			messageType: this.messageType,
			payload: this.payload
		};
		return json;
	}
}

class PlayerServerMessage {
	constructor(payload) {
		this.messageType = payload.messageType;
		if (!PlayerServerMessage.MESSAGE_TYPES[this.messageType]) {
			throw 'Invalid PlayerServerMessage message type';
		}
		this.payload = payload.payload;
	}

	static get MESSAGE_TYPES() {
		return {
			TableStatusRequest: 'TableStatusRequest',
			CreateTeam: 'CreateTeam',
			JoinTeam: 'JoinTeam',
			AnswerQuestion: 'AnswerQuestion',
			GradeQuestion: 'GradeQuestion'
		}
	}

	/**
	 * toJSON
	 * Returns a JSON object representation of the PlayerServerMessage.
	 */
	toJSON() {
		let json = {
			messageType: this.messageType,
			payload: this.payload
		};
		return json;
	}
}

module.exports = {
	WSClientMessage,
	HostServerMessage,
	PlayerServerMessage,
	ClientServerChatMessage
}