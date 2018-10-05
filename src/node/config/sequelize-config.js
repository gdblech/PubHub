const fs = require('fs');

module.exports = {
	development: {
		username: 'root',
		password: 'my-secret-pw',
		database: 'pubhub-dev',
		host: '127.0.0.1',
		dialect: 'mysql',
		seederStorage: 'sequelize'
		// seederStorageTableName: 'sequelize_data'
	},
	production: {
		username: process.env.RDS_USERNAME,
		password: process.env.RDS_PASSWORD,
		database: process.env.RDS_DB_NAME,
		host: process.env.RDS_HOSTNAME,
		dialect: 'mysql',
		logging: false,
		seederStorage: 'sequelize'
		// seederStorageTableName: 'sequelize_data'
	}
};