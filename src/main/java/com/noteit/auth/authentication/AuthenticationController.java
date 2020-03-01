package com.noteit.auth.authentication;

import com.noteit.auth.Authorities;
import com.noteit.auth.AuthoritiesService;
import com.noteit.auth.Users;
import com.noteit.auth.UsersService;
import com.noteit.auth.authorization.AuthorizationPrivilege;
import com.noteit.auth.authorization.PasswordResetToken;
import com.noteit.auth.authorization.PasswordTokenService;
import com.noteit.mail.MailService;
import com.noteit.util.CommonUtil;
import com.noteit.util.StringUtil;
import com.noteit.util.ValidatorUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/authn")
public class AuthenticationController
{
    private static final Logger logger = Logger.getLogger(AuthenticationController.class.getName());

    @Autowired
    private AuthenticationControllerErrorHandler errorHandler;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordTokenService passwordTokenService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private AuthoritiesService authoritiesService;

    @PostMapping(path = "/signup")
    public void signUp(HttpServletRequest request, HttpServletResponse response)
    {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (!ValidatorUtil.isValidEmail(username))
        {
            errorHandler.sendResponse(response, AuthenticationControllerErrorHandler.INVALID_EMAIL);
            return;
        }
        if (!ValidatorUtil.isValidPassword(password))
        {
            errorHandler.sendResponse(response, AuthenticationControllerErrorHandler.INVALID_PASSWORD);
            return;
        }

        Users user = new Users(username, password);
        if (usersService.isExists(user))
        {
            errorHandler.sendResponse(response, AuthenticationControllerErrorHandler.EMAIL_EXISTS);
            return;
        }

        Authorities authority = new Authorities();
        authority.setUser(user);
        authority.setAuthority("USER");
        try
        {
            authoritiesService.addAuthority(authority);
            CommonUtil.sendSuccessResponse(response);
        }
        catch (Exception ex)
        {
            logger.log(Level.SEVERE, "Unable to sign up user.", ex);
            errorHandler.sendResponse(response, AuthenticationControllerErrorHandler.SERVER_ERROR);
        }
    }

    @PostMapping(path = "/change-password")
    public void changePassword(HttpServletRequest request, HttpServletResponse response)
    {
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");

        Users users = usersService.getCurrentUser();
        if (!usersService.checkPassword(oldPassword))
        {
            errorHandler.sendResponse(response, AuthenticationControllerErrorHandler.INCORRECT_PASSWORD);
            return;
        }
        if (!ValidatorUtil.isValidPassword(newPassword))
        {
            errorHandler.sendResponse(response, AuthenticationControllerErrorHandler.INVALID_PASSWORD);
            return;
        }

        try
        {
            users.setPassword(newPassword);
            usersService.updateUser(users);
            CommonUtil.sendSuccessResponse(response);
        }
        catch (Exception ex)
        {
            logger.log(Level.SEVERE, "Unable to change user password.", ex);
            errorHandler.sendResponse(response, ex.getMessage());
        }
    }

    @PostMapping(path = "/forgot-password")
    public void forgotPassword(@RequestBody Map<String, Object> requestParam, HttpServletResponse response)
    {
        String email = (String) requestParam.get("email");
        if (!ValidatorUtil.isValidEmail(email))
        {
            errorHandler.sendResponse(response, AuthenticationControllerErrorHandler.INVALID_EMAIL);
            return;
        }

        Users user;
        try
        {
            user = usersService.getUser(email);
        }
        catch (Exception ex)
        {
            errorHandler.sendResponse(response, ex.getMessage());
            return;
        }

        PasswordResetToken token = passwordTokenService.getResetToken(user);
        try
        {
            mailService.sendPasswordResetToken(token);
            CommonUtil.sendSuccessResponse(response);
        }
        catch (Exception ex)
        {
            logger.log(Level.SEVERE, "Unable to Email the reset password token.", ex);
            errorHandler.sendResponse(response, ex.getMessage());
        }
    }

    @GetMapping(path = "/verify-token")
    public RedirectView verifyToken(HttpServletRequest request)
    {
        String id = request.getParameter("id");
        String token = request.getParameter("token");
        if (StringUtil.isBlank(id) || StringUtil.isBlank(token))
        {
            return errorHandler.getRedirectView(AuthenticationControllerErrorHandler.LOGIN_PAGE);
        }

        Long userId = Long.parseLong(id);
        if (!passwordTokenService.isValid(userId, token))
        {
            return errorHandler.getRedirectView(AuthenticationControllerErrorHandler.LOGIN_PAGE);
        }

        try
        {
            Authentication authentication = new UsernamePasswordAuthenticationToken(usersService.getUser(userId), null, Collections.singletonList(new SimpleGrantedAuthority(AuthorizationPrivilege.CHANGE_PASSWORD)));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        catch (Exception ex)
        {
            logger.log(Level.SEVERE, "Unable to verify password reset token.", ex);
            return errorHandler.getRedirectView(AuthenticationControllerErrorHandler.LOGIN_PAGE);
        }
        return errorHandler.getRedirectView(AuthenticationControllerErrorHandler.PWD_RESET_PAGE);
    }

    @PostMapping(path = "/reset-password")
    public void resetPassword(@RequestBody Map<String, String> params, HttpServletResponse response)
    {
        String password = params.get("password");
        if (!ValidatorUtil.isValidPassword(password))
        {
            errorHandler.sendResponse(response, AuthenticationControllerErrorHandler.INVALID_PASSWORD);
            return;
        }

        try
        {
            usersService.changePassword(password);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Users user = (Users) authentication.getPrincipal();
            passwordTokenService.removeToken(user);
            CommonUtil.sendSuccessResponse(response);
        }
        catch (Exception ex)
        {
            errorHandler.sendResponse(response, ex.getMessage());
        }
    }
}
