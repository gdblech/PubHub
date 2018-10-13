const log4js = require('log4js');
const express = require('express');
const morgan = require('morgan');
const bearerToken = require('express-bearer-token');
const authMiddleware = require('./utils/authMiddleware');
const WebSocketHandler = require('./controllers/websocket/WebSocketHandler');
const Authentication = require('./controllers/authentication');

let logger = log4js.getLogger();
logger.level = process.env.LOG_LEVEL || 'info';
const app = express();
const port = 8081;


app.use(morgan('common'));
app.use(bearerToken());
// authMiddleware needs to be after bearerToken
app.use(authMiddleware);


app.get('/', (req, res) => res.send('Test'));

let authRoute = require('./routes/auth.js');
app.use('/api/auth', authRoute);

app.listen(port, () => logger.info(`Example app listening on port ${port}!`));

let wsh = new WebSocketHandler(8082, Authentication.validate);