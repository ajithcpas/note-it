import Ember from "ember";

export default Ember.Object.create({
  htmlEncode(text)
  {
    return text.replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;")
      .replace(/'/g, "&apos;")
      .replace(/ /g, "&nbsp;")
      .replace(/\n/g, "</br>");
  },

  htmlDecode(text)
  {
    return text.replace(/&lt;/g, "<")
      .replace(/&gt;/g, ">")
      .replace(/&quot;/g, "\"")
      .replace(/&apos;/g, "'")
      .replace(/&nbsp;/g, " ")
      .replace(/&amp;/g, "&");
  },

  removeTags(str)
  {
    if ((str === null) || (str === ""))
    {
      return str;
    }
    else
    {
      str = str.toString();
      return str.replace(/<\/br>/g, "\n")
        .replace(/(<([^>]+)>)/ig, "");
    }
  }
});
