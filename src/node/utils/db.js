const config = require('config');
const AWS = require('aws-sdk');

let awsConfig = {
	region: config.get('AWS.region')
}

if (process.env.DB_PATH) {
	awsConfig.endpoint = process.env.DB_PATH;
}

AWS.config.update(awsConfig);

createTable = async (table) => {
	let ddb = new AWS.DynamoDB();
	data = await ddb.createTable(table).promise();
	return data;
};

getTables = async () => {
	let ddb = new AWS.DynamoDB();
	var params = {};
	data = await ddb.listTables(params).promise();
	return data;
};

getUser = async (email) => {
	let params = {
		TableName: config.get('DB.userTable'),
		Key: {
			email: email
		}
	};
	let docClient = new AWS.DynamoDB.DocumentClient();
	let data = await docClient.get(params).promise();
	return data;
};

createUser = async (user) => {
	let docClient = new AWS.DynamoDB.DocumentClient();
	let params = {
		TableName: config.get('DB.userTable'),
		Item: user
	};

	let data = await docClient.put(params).promise();
	return data;
}

module.exports = {
	createTable,
	getTables,
	getUser,
	createUser
}