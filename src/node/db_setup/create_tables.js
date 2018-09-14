const config = require('config');
const db = require('../utils/db.js');

let tables = require('./tables.json');

let createTables = async (tables) => {
	for (let i = 0; i < tables.length; i++) {
		let table = tables[i];
		table.TableName = config.get(table.TableName);
		try {
			await db.createTable(table);
		} catch (err) {
			console.error(err.message);
		}
	}
};

createTables(tables);