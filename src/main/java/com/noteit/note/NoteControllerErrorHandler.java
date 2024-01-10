package com.noteit.note;

import com.noteit.util.CommonUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class NoteControllerErrorHandler {
    public static final String INVALID_DATA = "invalid_data";
    public static final String NOTE_NOT_EXISTS = "note_not_exists";
    public static final String TITLE_LIMIT_EXCEEDS = "title_limit_exceeds";
    public static final String SEARCH_QUERY_LIMIT_EXCEEDS = "search_query_limit_exceeds";
    public static final String SERVER_ERROR = "server_error";

    public void sendResponse(HttpServletResponse response, String errorCode) {
        String errorMsg = switch (errorCode) {
            case INVALID_DATA -> "No data provided.";
            case NOTE_NOT_EXISTS -> "Requested Note doesn't exists.";
            case TITLE_LIMIT_EXCEEDS -> "Title must be less than 250 characters.";
            case SEARCH_QUERY_LIMIT_EXCEEDS -> "Search query must be less than 250 characters.";
            default -> "Something went wrong. Try again later.";
        };
        CommonUtil.sendErrorResponse(response, errorCode, errorMsg);
    }
}
