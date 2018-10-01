const log4js = require('log4js');
let logger = log4js.getLogger();
logger.level = process.env.LOG_LEVEL || 'info';

const express = require('express');
const app = express();
const port = 8081;
const morgan = require('morgan');
const bearerToken = require('express-bearer-token');
const auth_mw = require('./utils/auth_mw');

app.use(morgan('common'));
app.use(bearerToken());
// auth_mw needs to be after bearerToken
app.use(auth_mw);


app.get('/', (req, res) => res.send('Test'));

let authRoute = require('./routes/auth.js');
app.use('/api/auth', authRoute);

app.listen(port, () => logger.info(`Example app listening on port ${port}!`));