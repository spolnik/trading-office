var restify = require('restify'),
    Datastore = require('nedb'),
    db = new Datastore({filename: 'confirmations.db', autoload: true}),
    server = restify.createServer(),
    Eureka = require('eureka-js-client').Eureka;

server.use(restify.acceptParser(server.acceptable));
server.use(restify.queryParser());
server.use(restify.bodyParser());

var client = new Eureka({
    instance: {
        app: 'CONFIRMATION-SERVICE',
        hostName: 'localhost',
        ipAddr: '127.0.0.1',
        port: 9013,
        vipAddress: 'jq.test.something.com',
        dataCenterInfo: {
            name: 'MyOwn'
        }
    },
    eureka: {
        // eureka server host / port
        host: 'localhost',
        port: 5000
    }
});

client.start();

function getConfirmation(req, res, next) {

    db.find({allocationId: req.params.id}, function (err, docs) {
        res.send(docs[0]);
        return next();
    });
}

function addConfirmation(req, res, next) {

    var confirmation = req.body;
    db.insert(confirmation);
    res.send(201, confirmation);
    return next();
}

function getConfirmationIds(req, res, next) {

    db.find({}, function (err, docs) {
        res.send(docs.map(function(x) { return x._id; }));
        return next();
    });
}

server.post('/api/confirmation', addConfirmation);
server.get('/api/confirmation/ids', getConfirmationIds);
server.get('/api/confirmation/:id', getConfirmation);

var port = process.env.PORT || 8080;
server.listen(port, function () {
    console.log('%s listening at %s', server.name, server.url);
});