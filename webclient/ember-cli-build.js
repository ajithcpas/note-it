/*jshint node:true*/
/* global require, module */
var EmberApp = require("ember-cli/lib/broccoli/ember-app");

module.exports = function (defaults) {
  var app = new EmberApp(defaults, {
    fingerprint: {
      enabled: false
    }
  });

  app.import("bower_components/bootstrap/dist/css/bootstrap.min.css");
  app.import("bower_components/bootstrap/dist/js/bootstrap.min.js");
  return app.toTree();
};
