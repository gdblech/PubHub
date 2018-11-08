'use strict';
module.exports = (sequelize, DataTypes) => {
	const ChatMessage = sequelize.define('ChatMessage', {
		message: DataTypes.STRING,
		timestamp: DataTypes.DATE
	}, {});
	ChatMessage.associate = function (models) {
		// associations can be defined here
		ChatMessage.belongsTo(models.User);
	};

	ChatMessage.prototype.toJSON = function () {
		let json = {};
		json.message = this.message;
		json.timestamp = this.timestamp;
		if (this.User) {
			json.user = this.User.userName;
		}
		return json;
	}

	return ChatMessage;
};