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
      this.set("hasError", this.get("fieldStatus") === "error");
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
