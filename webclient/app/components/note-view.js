import Ember from "ember";
import CommonUtil from "../utils/common-util";

export default Ember.Component.extend({
  classNames: "note",

  didRender()
  {
    Ember.$('[data-toggle="tooltip"]').tooltip();
  },

  deleteNote(id)
  {
    this.sendAction("ondelete", id);
  },

  actions: {
    editNote(note)
    {
      let tempNote = Ember.$.extend({}, true, note);
      Ember.set(tempNote, "editing", true);
      if (tempNote.title)
      {
        Ember.set(tempNote, "title", CommonUtil.htmlDecode(CommonUtil.removeTags(tempNote.title)));
      }
      Ember.set(tempNote, "data", CommonUtil.htmlDecode(CommonUtil.removeTags(tempNote.data)));
      this.sendAction("onedit", tempNote);
    },

    confirmDelete(id)
    {
      let params = {
        text: "Do you want to delete the note?",
        confirmButton: "Yes",
        cancelButton: "No",
        data: id,
        confirmAction: this.get("deleteNote").bind(this)
      };
      this.get("alertService").confirmDialog(params);
    }
  }
});
