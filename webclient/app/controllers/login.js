import Ember from "ember";
import ValidationUtil from "../utils/validation-util";

export default Ember.Controller.extend({
  showLoginPage: true,

  resetFields()
  {
    this.set("username", null);
    this.set("password", null);
    this.set("confirmPassword", null);
    this.set("emailStatus", null);
    this.set("passwordStatus", null);
    this.set("cnfPasswordStatus", null);
  },

  actions: {
    login()
    {
      let self = this;
      let username = self.get("username");
      if (ValidationUtil.isBlank(username))
      {
        self.set("emailStatus", "error");
        self.get("alertService").failure("Email address field cannot be empty.");
        return;
      }
      if (!ValidationUtil.isValidEmail(username))
      {
        self.set("emailStatus", "error");
        self.get("alertService").failure("Invalid Email address.");
        return;
      }
      let password = self.get("password");
      if (!ValidationUtil.isValidPassword(password))
      {
        self.set("passwordStatus", "error");
        self.get("alertService").failure("Password must be between 6 and 25 characters.");
        return;
      }

      self.get("authService").login(username, password)
        .then(() => {
          if (self.get("authService.isAuthenticated"))
          {
            self.resetFields();
            self.transitionToRoute("root.home");
          }
        })
        .catch((error) => {
          if (error.status === 401)
          {
            self.get("alertService").failure("Invalid Email address or password.");
          }
        });
    },

    signUp()
    {
      let self = this;
      let username = self.get("username");
      if (ValidationUtil.isBlank(username))
      {
        self.set("emailStatus", "error");
        self.get("alertService").failure("Email address field cannot be empty.");
        return;
      }
      if (!ValidationUtil.isValidEmail(username))
      {
        self.set("emailStatus", "error");
        self.get("alertService").failure("Invalid Email address.");
        return;
      }
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

      self.get("authService").signUp(self.get("username"), self.get("password"))
        .then(() => {
          self.get("alertService").success("Account created successfully.");
          self.send("showLoginPage", true);
        })
        .catch((error) => {
          if (error.error === "username_exist")
          {
            self.get("alertService").failure("Username already exists. Please use different username.");
          }
          else
          {
            self.get("alertService").failure(error.errorMsg);
          }
        });
    },

    forgotPassword()
    {
      let self = this;
      let username = self.get("username");
      if (ValidationUtil.isBlank(username))
      {
        self.set("emailStatus", "error");
        self.get("alertService").failure("Email address field cannot be empty.");
        return;
      }
      if (!ValidationUtil.isValidEmail(username))
      {
        self.set("emailStatus", "error");
        self.get("alertService").failure("Invalid Email address.");
        return;
      }

      self.get("authService").forgotPassword(username)
        .then((response) => {
          if (response.status === "success")
          {
            self.get("alertService").success("Password reset link has been mailed to your Email address.", false);
          }
        }).catch((error) => {
        self.get("alertService").failure(error.errorMsg);
      });
    },

    showLoginPage(value)
    {
      this.set("showLoginPage", value);
      this.resetFields();
    }
  }
});
