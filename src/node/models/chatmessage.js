'use strict';
module.exports = (sequelize, DataTypes) => {
  const ChatMessage = sequelize.define('ChatMessage', {
    message: DataTypes.STRING,
    timestamp: DataTypes.DATE
  }, {});
  ChatMessage.associate = function(models) {
    // associations can be defined here
    // ChatMessage.belongsTo(models.User);
  };
  return ChatMessage;
};