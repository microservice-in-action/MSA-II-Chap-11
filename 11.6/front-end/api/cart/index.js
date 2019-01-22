(function (){
  'use strict';

  var async     = require("async")
    , express   = require("express")
    , request   = require("request")
    , helpers   = require("../../helpers")
    , app       = express()
    , service   = require('../service')
    , localStorage = require('localStorage')

  // List items in cart for current logged in user.
  app.get("/cart", function (req, res, next) {
    console.log("Request received: " + req.url + ", " + req.query.custId);
    var custId = helpers.getCustomerId(req, app.get("env"));
    var username = localStorage.getItem("username") || "nouser";
    console.log("Customer ID: " + custId);
    console.log("username " + username);

     async.waterfall([
        function (callback) {
          var options = {
            headers: service.headers,
            uri: "http://orders/orders/" + custId + "/"+username,
            proxy: service.proxy,
            method: 'GET'
          };
          request(options, function (error, response, body) {
            if(error) {
              console.log("err in discount "+ error)
              callback(error);
              return;
            }
            console.log("discount is "+JSON.stringify(body));
            localStorage.setItem("shipping", body);
            var shipment = body;
            callback(error, shipment);
          });
        },
        function (shipment, callback) {
          console.log("shipment "+ shipment);
            var options = {
            headers: service.headers,
            uri: "http://carts/carts/" + custId + "/items",
            proxy: service.proxy,
            method: 'GET'
          };
          request(options, function (error, response, body) {
            if (error) {
              callback(error)
                return;
            }
            var data = {};
            console.log("show body and shipment");
            if (body == "") {
              data.cart = ""
            } else {
              data.cart = JSON.parse(body);
            }
            if (shipment == "") {
              data.shipment = "no shipment"
            } else {
              data.shipment = shipment;
            }
            console.log("status code " + response.statusCode)
            console.log("data.shipment"+ data.shipment);
            var cartDetails = JSON.stringify(data);
            callback(null, response.statusCode, cartDetails);
          });
        }
    ], function (err, statusCode, body) {
      if (err) {
        return next(err);
      }
      helpers.respondStatusBody(res, statusCode, body)
    });

  });

  // Delete cart
  app.delete("/cart", function (req, res, next) {
    var custId = helpers.getCustomerId(req, app.get("env"));
    console.log('Attempting to delete cart for user: ' + custId);
    var options = {
      headers: service.headers,
      uri: "http://carts/carts/" + custId,
      proxy: service.proxy,
      method: 'DELETE'
    };
    request(options, function (error, response, body) {
      if (error) {
        return next(error);
      }
      console.log('User cart deleted with status: ' + response.statusCode);
      helpers.respondStatus(res, response.statusCode);
    });
  });

  // Delete item from cart
  app.delete("/cart/:id", function (req, res, next) {
    if (req.params.id == null) {
      return next(new Error("Must pass id of item to delete"), 400);
    }

    console.log("Delete item from cart: " + req.url);

    var custId = helpers.getCustomerId(req, app.get("env"));

    var options = {
      headers: service.headers,
      uri: "http://carts/carts/" + custId + "/items/" + req.params.id.toString(),
      proxy: service.proxy,
      method: 'DELETE'
    };
    request(options, function (error, response, body) {
      if (error) {
        return next(error);
      }
      console.log('Item deleted with status: ' + response.statusCode);
      helpers.respondStatus(res, response.statusCode);
    });
  });

  // Add new item to cart
  app.post("/cart", function (req, res, next) {
    console.log("Attempting to add to cart: " + JSON.stringify(req.body));

    if (req.body.id == null) {
      next(new Error("Must pass id of item to add"), 400);
      return;
    }

    var custId = helpers.getCustomerId(req, app.get("env"));

    async.waterfall([
        function (callback) {
          var options = {
            headers: service.headers,
            uri: "http://catalogue/catalogues/" + req.body.id.toString(),
            proxy: service.proxy,
            method: 'GET'
        };
          request(options, function (error, response, body) {
            console.log(body);
            callback(error, JSON.parse(body));
          });
        },
        function (item, callback) {
          var options = {
            headers: service.headers,
            uri: "http://carts/carts/" + custId + "/items",
            proxy: service.proxy,
            method: 'POST',
            json: true,
            body: {itemId: item.id, unitPrice: item.price}
          };
          console.log("POST to carts: " + options.uri + " body: " + JSON.stringify(options.body));
          request(options, function (error, response, body) {
            if (error) {
              callback(error)
                return;
            }
            callback(null, response.statusCode);
          });
        }
    ], function (err, statusCode) {
      if (err) {
        return next(err);
      }
      if (statusCode != 201 && statusCode != 200) {
        return next(new Error("Unable to add to cart. Status code: " + statusCode))
      }
      helpers.respondStatus(res, statusCode);
    });
  });

// Update cart item
  app.post("/cart/update", function (req, res, next) {
    console.log("Attempting to update cart item: " + JSON.stringify(req.body));
    
    if (req.body.id == null) {
      next(new Error("Must pass id of item to update"), 400);
      return;
    }
    if (req.body.quantity == null) {
      next(new Error("Must pass quantity to update"), 400);
      return;
    }
    var custId = helpers.getCustomerId(req, app.get("env"));
	console.log("CustId befor the catalogue call ", custId);
    async.waterfall([
        function (callback) {
           var options = {
            headers: service.headers,
            uri: "http://catalogue/catalogue/" + req.body.id.toString(),
            proxy: service.proxy,
            method: 'GET'
          };
          request(options, function (error, response, body) {
            console.log(body);
            callback(error, JSON.parse(body));
          });
        },
        function (item, callback) {
	console.log("CustId after the catalogue call ", custId);
		custId = helpers.getCustomerId(req, app.get("env"));
	console.log("CustId recalling to the helpre method ", custId);
          var options = {
            headers: service.headers,
            uri: "http://carts/carts/" + custId + "/items",
            proxy: service.proxy,
            method: 'PATCH',
            json: true,
            body: {itemId: item.id, quantity: parseInt(req.body.quantity), unitPrice: item.price}
          };
          console.log("PATCH to carts: " + options.uri + " body: " + JSON.stringify(options.body));
          request(options, function (error, response, body) {
            if (error) {
              callback(error)
                return;
            }
            callback(null, response.statusCode);
          });
        }
    ], function (err, statusCode) {
      if (err) {
        return next(err);
      }
      if (statusCode != 202) {
        return next(new Error("Unable to add to cart. Status code: " + statusCode))
      }
      helpers.respondStatus(res, statusCode);
    });
  });
  
  module.exports = app;
}());
