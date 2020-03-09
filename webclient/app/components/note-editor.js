import Ember from "ember";
import AJAXUtil from "../utils/ajax-util";

export default Ember.Component.extend({
  alertService: Ember.inject.service("alert-service"),
  isEditing: Ember.observer("show", function () {
    if (this.get("show"))
    {
      Ember.$("#noteEditorModal").modal({
        backdrop: "static",
        keyboard: false
      });
    }
  }),

  isValidInput()
  {
    let title = Ember.$("#NEW_NOTE_TITLE").val().trim();
    let data = Ember.$("#NEW_NOTE_CONTENT").val().trim();
    if (!title && !data)
    {
      this.get("alertService").failure("Empty note cannot be saved.");
      return false;
    }
    return true;
  },

  actions: {
    close()
    {
      this.set("note.editing", false);
      Ember.$("#noteEditorModal").modal("hide");
    },

    addNote()
    {
      if (!this.isValidInput())
      {
        return;
      }
      let title = Ember.$("#NEW_NOTE_TITLE").val().trim();
      let data = Ember.$("#NEW_NOTE_CONTENT").val().trim();
      let self = this;
      AJAXUtil.post("/home/add", {title: title, data: data})
        .then((response) => {
          if (response.status)
          {
            self.get("alertService").success("Note created.");
            self.send("close");
            self.sendAction("save");
          }
          else if (response.error)
          {
            self.get("alertService").failure(response.errorMsg);
          }
        });
    },

    updateNote(id)
    {
      if (!this.isValidInput())
      {
        return;
      }
      let title = Ember.$("#NEW_NOTE_TITLE").val().trim();
      let data = Ember.$("#NEW_NOTE_CONTENT").val().trim();
      let self = this;
      AJAXUtil.put("/home/" + id, {title: title, data: data})
        .then((response) => {
          if (response.status)
          {
            self.get("alertService").success("Note updated.");
            self.send("close");
            self.sendAction("save");
          }
          else if (response.error)
          {
            self.get("alertService").failure(response.errorMsg);
          }
        });
    }
  }
});
