package com.noteit.note;

import com.noteit.util.CommonUtil;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class NoteControllerErrorHandler
{
    public static final String INVALID_DATA = "invalid_data";
    public static final String NOTE_NOT_EXISTS = "note_not_exists";
    public static final String TITLE_LIMIT_EXCEEDS = "title_limit_exceeds";
    public static final String SEARCH_QUERY_LIMIT_EXCEEDS = "search_query_limit_exceeds";
    public static final String SERVER_ERROR = "server_error";

    public void sendResponse(HttpServletResponse response, String errorCode)
    {
        String errorMsg;
        switch (errorCode)
        {
            case INVALID_DATA:
                errorMsg = "No data provided.";
                break;
            case NOTE_NOT_EXISTS:
                errorMsg = "Requested Note doesn't exists.";
                break;
            case TITLE_LIMIT_EXCEEDS:
                errorMsg = "Title must be less than 250 characters.";
                break;
            case SEARCH_QUERY_LIMIT_EXCEEDS:
                errorMsg = "Search query must be less than 250 characters.";
                break;
            case SERVER_ERROR:
            default:
                errorMsg = "Something went wrong. Try again later.";
        }
        CommonUtil.sendErrorResponse(response, errorCode, errorMsg);
    }
}
