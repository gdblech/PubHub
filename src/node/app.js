const log4js = require('log4js');
let logger = log4js.getLogger();
logger.level = process.env.LOG_LEVEL;

const express = require('express');
const app = express();
const port = 8081;
const morgan = require('morgan');
app.use(morgan('common'));
app.get('/', (req, res) => res.send('Test'));

let userRoute = require('./routes/users.js');
app.use('/api/user', userRoute);

app.listen(port, () => logger.info(`Example app listening on port ${port}!`));