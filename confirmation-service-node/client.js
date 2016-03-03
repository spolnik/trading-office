var assert = require('assert');
var restify = require('restify');

var client = restify.createJsonClient({
    url: 'http://localhost:8080'
});

var confirmation = {
    allocationId: "12345",
    customField: "ABC"
};

client.post('/api/confirmation', confirmation, function (err, req, res, obj) {
    assert.ifError(err);
    console.log('Server returned: %j', obj);
});

client.get('/api/confirmation/12345', function (err, req, res, obj) {
    assert.ifError(err);
    console.log('Server returned: %j', obj);
});