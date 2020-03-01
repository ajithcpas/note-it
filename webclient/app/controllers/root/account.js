import Ember from "ember";
import ValidationUtil from "../../utils/validation-util";

export default Ember.Controller.extend({
  oldPassword: null,
  newPassword: null,
  confirmPassword: null,

  resetFields()
  {
    this.set("oldPassword", null);
    this.set("newPassword", null);
    this.set("confirmPassword", null);
  },

  actions: {
    changePassword()
    {
      let self = this;
      let oldPassword = self.get("oldPassword");
      if (!ValidationUtil.isValidPassword(oldPassword))
      {
        self.set("oldPasswordStatus", "error");
        self.get("alertService").failure("Password must be between 6 and 25 characters.");
        return;
      }
      let newPassword = self.get("newPassword");
      if (!ValidationUtil.isValidPassword(newPassword))
      {
        self.set("newPasswordStatus", "error");
        self.get("alertService").failure("Password must be between 6 and 25 characters.");
        return;
      }
      let cnfPassword = self.get("confirmPassword");
      if (cnfPassword !== newPassword)
      {
        self.set("cnfPasswordStatus", "error");
        self.get("alertService").failure("Password doesn't match.");
        return;
      }

      self.get("authService").changePassword(self.get("oldPassword"), self.get("newPassword"))
        .then(() => {
          self.get("alertService").success("Password changed successfully.");
          self.send("gotoHome");
        })
        .catch((error) => {
          self.get("alertService").failure(error.errorMsg);
        });
    },

    gotoHome()
    {
      this.resetFields();
      Ember.$(".navbar").show();
      this.transitionToRoute("root.home");
    }
  }
});
