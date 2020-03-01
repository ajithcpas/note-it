import Ember from "ember";

export default Ember.Route.extend({
  beforeModel()
  {
    this.replaceWith("root.home");
  },

  actions: {
    logout()
    {
      let self = this;
      this.get("authService").logout()
        .then(() => {
          self.get("alertService").success("You have been logged out successfully.");
          self.transitionTo("login");
        })
        .catch((error) => {
          self.get("alertService").failure(error);
        });
    }
  }
});
