import Ember from "ember";
import ValidationUtil from "../utils/validation-util";

export default Ember.Controller.extend({
  resetFields()
  {
    this.set("password", null);
    this.set("confirmPassword", null);
  },

  actions: {
    resetPassword()
    {
      let self = this;
      let password = self.get("password");
      if (!ValidationUtil.isValidPassword(password))
      {
        self.set("passwordStatus", "error");
        self.get("alertService").failure("Password must be between 6 and 25 characters.");
        return;
      }
      let cnfPassword = self.get("confirmPassword");
      if (cnfPassword !== password)
      {
        self.set("cnfPasswordStatus", "error");
        self.get("alertService").failure("Password doesn't match.");
        return;
      }

      self.get("authService").resetPassword(self.get("password"))
        .then(() => {
          self.get("alertService").success("Password changed successfully.");
          self.resetFields();
          self.transitionToRoute("login");
        })
        .catch((error) => {
          if (error.status)
          {
            if (error.status === 403)
            {
              self.resetFields();
              self.transitionToRoute("root.home");
              return;
            }
            else if (error.status === 401)
            {
              self.get("alertService").failure("You're password reset link might have expired. Click forgot password from the login page.", false);
              self.resetFields();
              return;
            }
          }
          self.get("alertService").failure(error.errorMsg);
        });
    },

    cancel()
    {
      this.resetFields();
      this.transitionToRoute("login");
    }
  }
});
