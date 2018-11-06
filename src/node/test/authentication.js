const chai = require('chai');
const assert = chai.assert;
const sinon = require('sinon');
const log4js = require('log4js');
let logger = log4js.getLogger();
logger.level = process.env.LOG_LEVEL || 'info';

const authentication = require('../controllers/authentication');
let dependencies = authentication.getDependencies();

const jwt = require('jsonwebtoken');

describe('Test authentication controller', function () {
	describe('Test authentication.validate()', function () {
		let validJWT;
		before(async function () {
			validJWT = jwt.sign({
				"userId": "104463408769641250676",
				"userName": "keshek@gmail.com",
				"role": {
					"roleName": "Customer"
				}
			}, process.env.JWT_KEY, {
				expiresIn: '6h'
			});
		});
	});
});