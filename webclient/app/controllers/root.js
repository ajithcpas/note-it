import Ember from "ember";

export default Ember.Controller.extend({
  homeController: Ember.inject.controller("root.home"),
  searchNote: null,

  actions: {
    searchNote()
    {
      this.get("homeController").send("searchAllNote");
      // closes navbar toggle
      if (Ember.$(".navbar-toggle").css("display") !== "none")
      {
        Ember.$(".navbar-toggle").trigger("click");
      }
    },

    clearSearch()
    {
      this.get("homeController").send("getAllNote");
    },

    goto(route)
    {
      this.transitionToRoute(route);
    }
  }
});
