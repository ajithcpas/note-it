package com.noteit.auth.authentication;

import com.noteit.AppServerConfig;
import com.noteit.util.CommonUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

@Service
public class AuthenticationControllerErrorHandler {
    public static final String INVALID_EMAIL = "invalid_email";
    public static final String EMAIL_EXISTS = "email_exists";
    public static final String EMAIL_NOT_EXISTS = "email_not_exists";
    public static final String INVALID_PASSWORD = "invalid_password";
    public static final String INCORRECT_PASSWORD = "incorrect_password";
    public static final String SERVER_ERROR = "server_error";
    public static final int LOGIN_PAGE = 1;
    public static final int PWD_RESET_PAGE = 2;
    public static final int VERIFY_TOKEN_PAGE = 3;
    @Autowired
    private AppServerConfig appServerConfig;

    public void sendResponse(HttpServletResponse response, String errorCode) {
        String errorMsg = switch (errorCode) {
            case INVALID_EMAIL -> "Invalid Email address.";
            case EMAIL_EXISTS -> "Given Email address already exists. Please provide a different Email address.";
            case EMAIL_NOT_EXISTS -> "No account configured for the given Email address.";
            case INVALID_PASSWORD -> "Password must be between 6 and 25 characters.";
            case INCORRECT_PASSWORD -> "The old password is incorrect.";
            default -> "Something went wrong. Try again later!";
        };
        CommonUtil.sendErrorResponse(response, errorCode, errorMsg);
    }

    public RedirectView getRedirectView(int view) {
        return switch (view) {
            case PWD_RESET_PAGE -> new RedirectView(appServerConfig.getServerUrl() + "/index.html#/reset-password");
            case VERIFY_TOKEN_PAGE ->
                    new RedirectView(appServerConfig.getServerUrl() + "/index.html#/verify-token?error=invalid");
            default -> new RedirectView(appServerConfig.getServerUrl() + "/index.html#/login");
        };
    }
}
