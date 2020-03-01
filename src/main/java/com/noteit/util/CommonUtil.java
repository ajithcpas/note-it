package com.noteit.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CommonUtil
{
    private static final Logger logger = Logger.getLogger(CommonUtil.class.getName());

    private CommonUtil()
    {
    }

    public static void sendErrorResponse(HttpServletResponse response, String errorCode, String errorMsg)
    {
        JSONObject result = new JSONObject().put("error", errorCode).put("errorMsg", errorMsg);
        sendResponse(response, result);
    }

    public static void sendSuccessResponse(HttpServletResponse response)
    {
        JSONObject result = new JSONObject().put("status", "success");
        sendResponse(response, result);
    }

    public static void sendResponse(HttpServletResponse response, Object object)
    {
        if (object instanceof JSONObject)
        {
            sendResponse(response, object.toString());
            return;
        }

        ObjectMapper om = new ObjectMapper();
        try
        {
            CommonUtil.sendResponse(response, om.writeValueAsString(object));
        }
        catch (Exception ex)
        {
            logger.log(Level.SEVERE, "Unable to return HTTP response.", ex);
        }
    }

    public static void sendResponse(HttpServletResponse response, String result)
    {
        try
        {
            if (!response.isCommitted())
            {
                response.getWriter().print(result);
                response.flushBuffer();
            }
        }
        catch (Exception ex)
        {
            logger.log(Level.SEVERE, "Unable to return HTTP Response.", ex);
        }
    }
}
