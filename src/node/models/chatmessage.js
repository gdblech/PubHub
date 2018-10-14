'use strict';
const Messages = require('../controllers/websocket/Messages');
module.exports = (sequelize, DataTypes) => {
	const ChatMessage = sequelize.define('ChatMessage', {
		message: DataTypes.STRING,
		timestamp: DataTypes.DATE
	}, {});
	ChatMessage.associate = function (models) {
		// associations can be defined here
		ChatMessage.belongsTo(models.User);
	};
	ChatMessage.prototype.toWSMessage = function () {
		let wsMessage = new Messages.ServerClientChatMessage({

		});
	}
	return ChatMessage;
};