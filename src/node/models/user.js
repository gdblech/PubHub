'use strict';
const log4js = require('log4js');
let logger = log4js.getLogger();
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
		User.hasMany(models.ChatMessage);
		User.belongsToMany(models.Team, {
			through: 'TeamToUser'
		});
	};

	/**
	 * Updated findOrCreate method that can set Role and AuthType
	 * @param {*} params 
	 */
	User.findOrCreate2 = async (params) => {
		let user = await User.find({
			where: params.where
		});

		if (!user) {
			const Role = require('../models').Role;
			const AuthType = require('../models').AuthType;
			user = await User.create(params.defaults);
			logger.debug(`built user: ${JSON.stringify(user)}`);
			logger.debug(params.defaults.role);

			let role = await Role.find({
				where: {
					roleName: params.defaults.roleName
				}
			});

			let authType = await AuthType.find({
				where: {
					provider: params.defaults.authType
				}
			});

			user.setRole(role);
			user.setAuthType(authType);
		}
		return user;

	};

	/**
	 * Get a object with the JWT payload data for the user
	 */
	User.prototype.getJWTPayload = function () {
		let payload = {};
		payload.userId = this.userId;
		payload.userName = this.userName;
		return payload;
	};

	/**
	 * return the JSON representation of the user
	 */
	User.prototype.toJSON = function () {
		let json = {};
		json.userId = this.userId;
		json.userName = this.userName;
		json.email = this.email;
		if (this.profilePicPath) {
			json.profilePicPath = this.profilePicPath;
		}

		if (this.Role) {
			json.role = this.Role.roleName;
		}
		return json;
	};
	return User;
};