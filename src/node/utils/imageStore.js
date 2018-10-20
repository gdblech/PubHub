const AWS = require('aws-sdk');
const config = require('config');
const uuidv1 = require('uuid/v1');
const log4js = require('log4js');

let logger = log4js.getLogger();
logger.level = process.env.LOG_LEVEL || 'info';

let s3Bucket = new AWS.S3();

/**
 * put: Stores a image in the AWS S3 storage bucket specified in the config file. 
 * Assumes the image is of the type specified in the config file.
 * @param {*} data the data of the image file, expects either a base64 encoded
 * 		string (default) or a buffer of binary data.
 * @param {*} binary(optional) set to true if a binary buffer is sent instead of a base64
 * 		string.
 */
let put = async (data, binary) => {
	let key = uuidv1();
	let buffer = data;

	if (binary !== true) {
		buffer = Buffer.from(data, 'base64');
	}

	let params = {
		Bucket: config.get('S3Bucket'),
		Key: key,
		Body: buffer,
		ContentType: config.get('imageFormat')
	};

	try {
		await s3Bucket.putObject(params).promise();
	} catch (err) {
		logger.error(`[imageStore.get] ${err}`);
		throw err;
	}

	return key;
};

/**
 * get: retrieves an image from AWS based on the passed key, returns a base64 string
 * by default or a binary buffer if specified.
 * @param {*} key key of the image to load.
 * @param {*} binary(optional) specifies to return a binary buffer instead of a
 * 		base64 string.
 */
let get = async (key, binary) => {
	let params = {
		Bucket: config.get('S3Bucket'),
		Key: key
	}

	try {
		let data = await s3Bucket.getObject(params).promise();
		if (binary === true) {
			return data.Body;
		} else {
			return data.Body.toString('base64');
		}
	} catch (err) {
		logger.error(`[imageStore.get] ${err}`);
		throw err;
	}
};

module.exports = {
	put,
	get
};