import Ember from "ember";

export default Ember.Component.extend({
  alertService: Ember.inject.service("alert-service"),

  init()
  {
    this._super(...arguments);
    Ember.$(window).on("resize", this.get("resetAlertWidth"));
    this.get("alertService"); /* Need to call once to initialize the service */
  },

  didInsertElement()
  {
    this.resetAlertWidth();
  },

  resetAlertWidth()
  {
    let width = Ember.$(".alert").parent().width();
    Ember.$(".alert").outerWidth(width);
  },

  initParam: Ember.observer("alertService.alertParam", function () {
    let params = this.get("alertService.alertParam");
    if (!params)
    {
      this.send("close");
      return;
    }

    this.set("message", params.message);
    this.set("status", params.status);
    this.set("icon", params.status === "alert-danger" ? "glyphicon-remove-sign" : "glyphicon-ok-sign");
    this.set("autoHide", params.autoHide);
    this.set("timeout", params.timeout);
    this.send("open");
  }),

  actions: {
    open()
    {
      let self = this;
      Ember.run.cancel(self.get("autoHideTimer"));
      Ember.$(".alert").fadeIn("slow");
      if (self.get("autoHide"))
      {
        let autoHideTimer = Ember.run.later(function () {
          self.send("close");
        }.bind(self), self.get("timeout"));
        self.set("autoHideTimer", autoHideTimer);
      }
    },

    close()
    {
      Ember.$(".alert").fadeOut("slow");
    }
  }
});
