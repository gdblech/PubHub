'use strict';
module.exports = (sequelize, DataTypes) => {
	const User = sequelize.define('User', {
		userId: {
			type: DataTypes.STRING,
			allowNull: false,
			unique: true
		},
		userName: {
			type: DataTypes.STRING,
			allowNull: false,
			unique: true
		},
		email: {
			type: DataTypes.STRING,
			allowNull: false,
			unique: true
		},
		profilePicPath: DataTypes.STRING
	}, {});
	User.associate = function (models) {
		// associations can be defined here
		User.belongsTo(models.Role);
		User.belongsTo(models.AuthType);
	};
	return User;
};