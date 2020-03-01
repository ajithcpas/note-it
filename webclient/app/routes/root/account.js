import Ember from "ember";

export default Ember.Route.extend({
  actions: {
    didTransition()
    {
      Ember.$(".navbar").hide();
    }
  }
});
