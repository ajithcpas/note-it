package com.noteit.note;

import com.noteit.util.CommonUtil;
import com.noteit.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/home")
public class NoteController
{
    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteControllerErrorHandler errorHandler;

    @GetMapping
    public void getAllNote(HttpServletResponse response)
    {
        CommonUtil.sendResponse(response, replaceWithHTMLElement(noteService.getAllNote()));
    }

    @GetMapping(path = "/search")
    public void searchAllNote(HttpServletRequest request, HttpServletResponse response)
    {
        String search = request.getParameter("search");
        if (StringUtil.isBlank(search))
        {
            getAllNote(response);
            return;
        }
        if (search.trim().length() > 250)
        {
            errorHandler.sendResponse(response, NoteControllerErrorHandler.SEARCH_QUERY_LIMIT_EXCEEDS);
            return;
        }

        search = HtmlUtils.htmlEscape(search);
        CommonUtil.sendResponse(response, replaceWithHTMLElement(noteService.getAllNote(search)));
    }

    private List<Note> replaceWithHTMLElement(List<Note> notes)
    {
        for (Note note : notes)
        {
            note.setData(note.getData().replaceAll("\n", "</br>"));
        }
        return notes;
    }

    @PostMapping(path = "/add")
    public void addNote(@Valid @RequestBody Note note, HttpServletResponse response)
    {
        if (StringUtil.isBlank(note.getTitle()) && StringUtil.isBlank(note.getData()))
        {
            errorHandler.sendResponse(response, NoteControllerErrorHandler.INVALID_DATA);
            return;
        }

        if (!StringUtil.isBlank(note.getTitle()) && note.getTitle().trim().length() > 250)
        {
            errorHandler.sendResponse(response, NoteControllerErrorHandler.TITLE_LIMIT_EXCEEDS);
            return;
        }

        noteService.addNote(note);
        CommonUtil.sendSuccessResponse(response);
    }

    @PutMapping(value = "/{id}")
    public void updateNote(@PathVariable("id") Long id, @Valid @RequestBody Note note, HttpServletResponse response)
    {
        if (StringUtil.isBlank(note.getTitle()) && StringUtil.isBlank(note.getData()))
        {
            errorHandler.sendResponse(response, NoteControllerErrorHandler.INVALID_DATA);
            return;
        }
        if (!StringUtil.isBlank(note.getTitle()) && note.getTitle().trim().length() > 250)
        {
            errorHandler.sendResponse(response, NoteControllerErrorHandler.TITLE_LIMIT_EXCEEDS);
            return;
        }

        note.setId(id);
        try
        {
            noteService.updateNote(note);
            CommonUtil.sendSuccessResponse(response);
        }
        catch (Exception ex)
        {
            errorHandler.sendResponse(response, ex.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}")
    public void deleteNote(@PathVariable("id") Long id, HttpServletResponse response)
    {
        try
        {
            noteService.deleteNote(id);
            CommonUtil.sendSuccessResponse(response);
        }
        catch (Exception ex)
        {
            errorHandler.sendResponse(response, ex.getMessage());
        }
    }
}