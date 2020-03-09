import Ember from "ember";

export default Ember.Controller.extend({
  queryParams: ["error"],
  error: null,

  init()
  {
    this._super(...arguments);
    let self = this;
    Ember.run.scheduleOnce("afterRender", function () {
      let error = self.get("error");
      if (!error)
      {
        self.transitionToRoute("reset-password");
      }
      else
      {
        self.get("alertService").failure("Password reset link might be expired. Try again.");
        self.transitionToRoute("login");
      }
    });
  }
});
