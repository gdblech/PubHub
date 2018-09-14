let data = require('./data.json');
let db = require('../utils/db');
const async = require('async');

let createUsers = async (users) => {
	for (let i = 0; i < users.length; i++) {
		try {
			await db.createUser(users[i]);
		} catch (err) {
			console.error(err.message);
		}
	}
}

createUsers(data.users);