const log4js = require('log4js');
let logger = log4js.getLogger();
logger.level = process.env.LOG_LEVEL || 'info';

const express = require('express');
const app = express();
const port = 8081;
const morgan = require('morgan');
const bearerToken = require('express-bearer-token');
app.use(bearerToken());

app.use(morgan('common'));
app.get('/', (req, res) => res.send('Test'));

let userRoute = require('./routes/users.js');
app.use('/api/user', userRoute);

let authRoute = require('./routes/auth.js');
app.use('/api/auth', authRoute);

app.listen(port, () => logger.info(`Example app listening on port ${port}!`));