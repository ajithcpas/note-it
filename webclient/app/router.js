import Ember from "ember";
import config from "./config/environment";

const Router = Ember.Router.extend({
  location: config.locationType
});

Router.map(function () {
  this.route("root", {path: "/"}, function () {
    this.route("home");
    this.route("account");
  });
  this.route("login");
  this.route("reset-password");
  this.route("verify-token");
});

export default Router;
