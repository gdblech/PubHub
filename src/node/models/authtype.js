'use strict';
module.exports = (sequelize, DataTypes) => {
  const AuthType = sequelize.define('AuthType', {
    provider: DataTypes.STRING
  }, {});
  AuthType.associate = function(models) {
    // associations can be defined here
  };
  return AuthType;
};