import Ember from "ember";

export default Ember.Controller.extend({
  init()
  {
    Ember.$(document).ajaxStart(function () {
      Ember.$("#cover-spin").show();
    });

    Ember.$(document).ajaxStop(function () {
      Ember.$("#cover-spin").hide();
    });
  },

  actions: {
    focus(fieldName)
    {
      Ember.$("input[name=" + fieldName + "]").focus();
    }
  }
});
