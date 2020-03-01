import Ember from "ember";

export default Ember.Object.create({
  get(url, data, dataType = "json")
  {
    return this.ajaxRequest("GET", url, data, "application/json", dataType);
  },

  post(url, data, contentType = "application/json", dataType = "json")
  {
    if (contentType === "application/json")
    {
      data = JSON.stringify(data);
    }
    return this.ajaxRequest("POST", url, data, contentType, dataType);
  },

  put(url, data)
  {
    return this.ajaxRequest("PUT", url, JSON.stringify(data));
  },

  delete(url)
  {
    return this.ajaxRequest("DELETE", url);
  },

  ajaxRequest(type, url, data, contentType = "application/json", dataType = "json")
  {
    let async = true;
    let executor = function (resolve, reject) {
      let ajaxParam = {
        async: async,
        url: url,
        type: type,
        data: data,
        contentType: contentType,
        dataType: dataType,
        success: resolve,
        failure: reject,
        error: reject
      };
      Ember.$.ajax(ajaxParam);
    };
    return new Ember.RSVP.Promise(executor);
  }
});
