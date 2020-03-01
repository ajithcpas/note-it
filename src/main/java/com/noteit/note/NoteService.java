package com.noteit.note;

import com.noteit.auth.Users;
import com.noteit.auth.UsersService;
import com.noteit.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService
{
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UsersService usersService;

    public List<Note> getAllNote()
    {
        Users user = usersService.getCurrentUser();
        return noteRepository.findNoteByUserIdOrderByCreatedOnDesc(user.getId());
    }

    public List<Note> getAllNote(String search)
    {
        List<Note> result = new ArrayList<>();
        List<Note> notes = getAllNote();
        for (Note note : notes)
        {
            if (StringUtil.containsIgnoreCase(note.getTitle(), search) || StringUtil.containsIgnoreCase(note.getData(), search))
            {
                note.setTitle(highlight(note.getTitle(), search));
                note.setData(highlight(note.getData(), search));
                result.add(note);
            }
        }
        return result;
    }

    public String highlight(String data, String search)
    {
        if (!StringUtil.containsIgnoreCase(data, search))
        {
            return data;
        }
        int startPos = StringUtil.ignoreCaseIndexOf(data, search);
        int endPos = startPos + search.length();
        data = data.substring(0, startPos) + "<span class=\"highlight\">" + data.substring(startPos, endPos) + "</span>" + highlight(data.substring(endPos), search);
        return data;
    }

    public boolean addNote(Note note)
    {
        Users user = usersService.getCurrentUser();
        note.setUser(user);
        note.setTitle(HtmlUtils.htmlEscape(note.getTitle().trim()));
        note.setData(HtmlUtils.htmlEscape(note.getData().trim()));
        noteRepository.save(note);
        return true;
    }

    public boolean updateNote(Note note) throws Exception
    {
        Optional<Note> noteOptional = noteRepository.findById(note.getId());
        if (!noteOptional.isPresent())
        {
            throw new Exception(NoteControllerErrorHandler.NOTE_NOT_EXISTS);
        }
        Note existingNote = noteOptional.get();
        existingNote.setTitle(HtmlUtils.htmlEscape(note.getTitle().trim()));
        existingNote.setData(HtmlUtils.htmlEscape(note.getData().trim()));
        existingNote.setLastUpdatedOn(note.getLastUpdatedOn());
        noteRepository.save(existingNote);
        return true;
    }

    public Note deleteNote(Long id) throws Exception
    {
        Optional<Note> noteOptional = noteRepository.findById(id);
        if (!noteOptional.isPresent())
        {
            throw new Exception(NoteControllerErrorHandler.NOTE_NOT_EXISTS);
        }

        Note note = noteOptional.get();
        noteRepository.delete(note);
        return note;
    }
}
