'use strict';
module.exports = (sequelize, DataTypes) => {
	const Role = sequelize.define('Role', {
		roleName: DataTypes.STRING
	}, {});
	Role.associate = function (models) {
		// associations can be defined here
		Role.hasMany(models.User);
	};

	Role.prototype.toJSON = function () {
		let json = {};
		json.roleName = this.roleName;
		return json;
	};
	return Role;
};