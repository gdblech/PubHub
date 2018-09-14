const config = require('config');
const AWS = require('aws-sdk');
AWS.config.update({
	region: config.get('AWS.region')
});

module.exports = {
	getUser: async (email) => {
		let params = {
			TableName: config.get('DB.userTable'),
			Key: {
				email: email
			}
		};
		let docClient = new AWS.DynamoDB.DocumentClient();
		let data = await docClient.get(params).promise();
		return data;
	}
}