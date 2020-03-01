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

  actions: {
    close()
    {
      this.set("note.editing", false);
      Ember.$("#noteEditorModal").modal("hide");
    },

    addNote()
    {
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
