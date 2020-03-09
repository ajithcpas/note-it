package com.noteit.auth.authentication;

import com.noteit.AppServerConfig;
import com.noteit.util.CommonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;

@Service
public class AuthenticationControllerErrorHandler
{
    @Autowired
    private AppServerConfig appServerConfig;

    public static final String INVALID_EMAIL = "invalid_email";
    public static final String EMAIL_EXISTS = "email_exists";
    public static final String EMAIL_NOT_EXISTS = "email_not_exists";
    public static final String INVALID_PASSWORD = "invalid_password";
    public static final String INCORRECT_PASSWORD = "incorrect_password";
    public static final String SERVER_ERROR = "server_error";

    public static final int LOGIN_PAGE = 1;
    public static final int PWD_RESET_PAGE = 2;
    public static final int VERIFY_TOKEN_PAGE = 3;

    public void sendResponse(HttpServletResponse response, String errorCode)
    {
        String errorMsg;
        switch (errorCode)
        {
            case INVALID_EMAIL:
                errorMsg = "Invalid Email address.";
                break;
            case EMAIL_EXISTS:
                errorMsg = "Given Email address already exists. Please provide a different Email address.";
                break;
            case EMAIL_NOT_EXISTS:
                errorMsg = "No account configured for the given Email address.";
                break;
            case INVALID_PASSWORD:
                errorMsg = "Password must be between 6 and 25 characters.";
                break;
            case INCORRECT_PASSWORD:
                errorMsg = "The old password is incorrect.";
                break;
            case SERVER_ERROR:
            default:
                errorMsg = "Something went wrong. Try again later!";
        }
        CommonUtil.sendErrorResponse(response, errorCode, errorMsg);
    }

    public RedirectView getRedirectView(int view)
    {
        switch (view)
        {
            case PWD_RESET_PAGE:
                return new RedirectView(appServerConfig.getServerUrl() + "/index.html#/reset-password");
            case VERIFY_TOKEN_PAGE:
                return new RedirectView(appServerConfig.getServerUrl() + "/index.html#/verify-token?error=invalid");
            case LOGIN_PAGE:
            default:
                return new RedirectView(appServerConfig.getServerUrl() + "/index.html#/login");
        }
    }
}
