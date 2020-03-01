import Ember from "ember";

export default Ember.Service.extend({
  /* Alert Dialog */
  alertParam: null,

  failure(message, autoHide = true, timeout = 5000)
  {
    this.alert(message, "alert-danger", autoHide, timeout);
  },

  success(message, autoHide = true, timeout = 5000)
  {
    this.alert(message, "alert-success", autoHide, timeout);
  },

  alert(message, status, autoHide = true, timeout = 5000)
  {
    let param = {
      message: message,
      status: status,
      autoHide: autoHide,
      timeout: timeout
    };
    this.set("alertParam", param);
  },

  close()
  {
    this.set("alertParam", null);
  },

  /* Confirm Dialog */
  confirmDialogParam: null,

  confirmDialog(params)
  {
    this.set("confirmDialogParam", params);
  }
});
