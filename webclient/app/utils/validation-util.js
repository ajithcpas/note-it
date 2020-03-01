import Ember from "ember";

export default Ember.Object.create({
  isBlank(text)
  {
    return text == null || text.trim().length === 0;
  },

  isValidEmail(email)
  {
    const EMAIL_PATTERN = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;
    return !this.isBlank(email) && EMAIL_PATTERN.test(email);
  },

  isValidPassword(password)
  {
    return !this.isBlank(password) && password.length > 5 && password.length < 26;
  }
});
