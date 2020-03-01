import Ember from "ember";

export default Ember.Component.extend({
  alertService: Ember.inject.service("alert-service"),

  init()
  {
    this._super(...arguments);
    this.get("alertService"); /* Need to call once to initialize the service */
  },

  initParam: Ember.observer("alertService.confirmDialogParam", function () {
    let params = this.get("alertService.confirmDialogParam");
    if (!params)
    {
      return;
    }

    this.set("title", params.title ? params.title : "Confirm Action");
    this.set("text", params.text ? params.text : "Do you want to proceed?");
    this.set("confirmButton", params.confirmButton ? params.confirmButton : "OK");
    this.set("cancelButton", params.cancelButton ? params.cancelButton : "Cancel");
    this.set("data", params.data);
    this.set("confirmAction", params.confirmAction);
    this.set("cancelAction", params.cancelAction);
    Ember.$("#confirmDialog").modal({backdrop: "static", keyboard: false});
  }),

  actions: {
    confirm()
    {
      let self = this;
      Ember.run.scheduleOnce("afterRender", () => {
        self.get("confirmAction")(self.get("data"));
        Ember.$("#confirmDialog").modal("hide");
      });
    },

    cancel()
    {
      let self = this;
      Ember.run.scheduleOnce("afterRender", () => {
        Ember.$("#confirmDialog").modal("hide");
        let cancelAction = self.get("cancelAction");
        if (cancelAction)
        {
          cancelAction();
        }
      });
    }
  }
});
