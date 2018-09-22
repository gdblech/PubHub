'use strict';
module.exports = (sequelize, DataTypes) => {
	const User = sequelize.define('User', {
		userId: DataTypes.STRING,
		userName: DataTypes.STRING,
		email: DataTypes.STRING
	}, {});
	User.associate = function (models) {
		// associations can be defined here
		User.belongsTo(models.Role);
		User.belongsTo(models.AuthType);
	};
	return User;
};