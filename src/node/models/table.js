'use strict';
module.exports = (sequelize, DataTypes) => {
  const Table = sequelize.define('Table', {
    qrCode: DataTypes.STRING
  }, {});
  Table.associate = function(models) {
    // associations can be defined here
    Table.hasMany(models.Team);
  };
  return Table;
};