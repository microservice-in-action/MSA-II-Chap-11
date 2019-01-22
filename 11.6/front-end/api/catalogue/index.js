(function (){
  'use strict';

  var express   = require("express")
    , request   = require("request")
    , helpers   = require("../../helpers")
    , app       = express()
    , service   = require("../service")

  console.log("**************************** got proxy ************************************"+service.proxy)
  app.use('/catalogues/images', express.static(__dirname + '/images'));

  app.get("/catalogues/images*", function (req, res, next) {
        res.send('<pre><a href="/images/WAT.jpg">WAT.jpg</a><br><a href="/images/WAT2.jpg">WAT2.jpg</a><br><a href="/images/bit_of_leg_1.jpeg">bit_of_leg_1.jpeg</a><br><a href="/images/bit_of_leg_2.jpeg">bit_of_leg_2.jpeg<a><br><a href="/images/catsocks.jpg">catsocks.jpg<a><br><a href="/images/catsocks2.jpg">catsocks2.jpg<a><br><a href="/images/classic.jpg">classic.jpg<a><br><a href="/images/classic2.jpg">classic2.jpg</a><br><a href="/images/colourful_socks.jpg">colourful_socks.jpg</a><br><a href="/images/cross_1.jpeg">cross_1.jpeg</a><br><a href="/images/cross_2.jpeg">cross_2.jpeg</a><br><a href="/images/holy_1.jpeg">holy_1.jpeg</a><br><a href="/images/holy_2.jpeg">holy_2.jpeg</a><br><a href="/images/holy_3.jpeg">holy_3.jpeg</a><br><a href="/images/puma_1.jpeg">puma_1.jpeg</a><br><a href="/images/puma_2.jpeg">puma_2.jpeg</a><br><a href="/images/rugby_socks.jpg">rugby_socks.jpg</a><br><a href="/images/sock.jpeg">sock.jpeg</a><br><a href="/images/youtube_1.jpeg">youtube_1.jpeg</a><br><a href="/images/youtube_2.jpeg">youtube_2.jpeg</a></pre>');
   });



/*app.get("/catalogue/images*", function (req, res, next) {
    var url = endpoints.catalogueUrl + req.url.toString();
    request.get(url)
        .on('error', function(e) { next(e); })
        .pipe(res);
  });
*/


  app.get("/catalogue*", function (req, res, next) {
    console.log("/catalogue* is called --> ");
    helpers.simpleHttpRequest("http://catalogue" +  req.url.toString(), res, next);
  });

  app.get("/tags", function(req, res, next) {
    helpers.simpleHttpRequest("http://catalogue/"+ 'tags', res, next);
  });

  module.exports = app;
}());
