import Ember from "ember";

export default Ember.Component.extend({
  hasSuccess: false,
  hasError: false,

  didUpdateAttrs()
  {
    this._super(...arguments);
    if (this.get("fieldStatus"))
    {
      this.set("hasSuccess", this.get("fieldStatus") === "success");
      if (this.get("fieldStatus") === "error")
      {
        this.set("hasError", true);
        Ember.$("input[name=" + this.get("name") + "]").focus();
      }
    }
    else
    {
      this.set("hasSuccess", false);
      this.set("hasError", false);
    }
  },

  actions: {
    onchange()
    {
      this.set("fieldStatus", null);
    }
  }
});
