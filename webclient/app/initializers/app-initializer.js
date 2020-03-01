import Ember from "ember";

export default {
  name: "app-initializer",
  initialize: function (container, application) {
    Ember.Route.reopen({
      authService: Ember.inject.service("auth-service"),
      alertService: Ember.inject.service("alert-service"),
    });

    Ember.Controller.reopen({
      authService: Ember.inject.service("auth-service"),
      alertService: Ember.inject.service("alert-service")
    });

    Ember.Component.reopen({
      alertService: Ember.inject.service("alert-service")
    });
  }
};
