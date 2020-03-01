import Ember from "ember";
import AJAXUtil from "../../utils/ajax-util";

export default Ember.Controller.extend({
  rootController: Ember.inject.controller("root"),
  notes: null,
  currNote: null,
  searchNote: Ember.computed.alias("rootController.searchNote"),
  isSearch: false,

  actions: {
    searchAllNote()
    {
      let self = this;
      let query = this.get("searchNote") ? {search: this.get("searchNote")} : null;
      this.set("isSearch", query != null);
      AJAXUtil.get("/home/search", query).then((response) => {
        if (response.error)
        {
          throw response;
        }
        self.set("notes", response);
      }).catch((error) => {
        if (error.status && error.status === 401)
        {
          self.transitionToRoute("login");
        }
        else if (error.error)
        {
          this.get("alertService").failure(error.errorMsg);
        }
      });
    },

    getAllNote()
    {
      let self = this;
      self.set("searchNote", null);
      self.set("isSearch", false);
      AJAXUtil.get("/home").then((response) => {
        self.set("notes", response);
      }).catch((error) => {
        if (error.status && error.status === 401)
        {
          self.transitionToRoute("login");
        }
      });
    },

    addNote()
    {
      this.set("currNote", {editing: true});
    },

    editNote(note)
    {
      note.editing = true;
      this.set("currNote", note);
    },

    deleteNote(id)
    {
      let self = this;
      AJAXUtil.delete("/home/" + id).then((response) => {
        if (response.error)
        {
          self.get("alertService").success(response.errorMsg);
          return;
        }
        self.get("alertService").success("Note deleted.");
        self.send("getAllNote");
      });
    }
  }
});
