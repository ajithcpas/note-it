import Ember from "ember";
import AJAXUtil from "../utils/ajax-util";

export default Ember.Service.extend({
  isAuthenticated: false,

  login(username, password)
  {
    let self = this;
    let data = {username: username, password: password};

    return AJAXUtil.post("/login", data, "application/x-www-form-urlencoded", "html")
      .then(() => {
        self.set("isAuthenticated", true);
      })
      .catch((error) => {
        self.set("isAuthenticated", false);
        if (error.status === 401)
        {
          throw error;
        }
      });
  },

  logout()
  {
    let self = this;
    return AJAXUtil.get("/logout", null, "html")
      .then(() => {
        self.set("isAuthenticated", false);
      });
  },

  signUp(username, password)
  {
    let data = {username: username, password: password};

    return AJAXUtil.post("/authn/signup", data, "application/x-www-form-urlencoded")
      .then((response) => {
        if (response.error)
        {
          throw response;
        }
      });
  },

  changePassword(oldPassword, newPassword)
  {
    let data = {oldPassword: oldPassword, newPassword: newPassword};
    return AJAXUtil.post("/authn/change-password", data, "application/x-www-form-urlencoded")
      .then((response) => {
        if (response.error)
        {
          throw response;
        }
      });
  },

  forgotPassword(username)
  {
    let data = {email: username};
    return AJAXUtil.post("/authn/forgot-password", data)
      .then((response) => {
        if (response.error)
        {
          throw response;
        }
        return response;
      });
  },

  resetPassword(password)
  {
    let data = {password: password};
    return AJAXUtil.post("/authn/reset-password", data)
      .then((response) => {
        if (response.error)
        {
          throw response;
        }
        return response;
      });
  }
});
