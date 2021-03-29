package com.upgrad.quora.service.business.impl;

import com.upgrad.quora.service.business.JwtTokenProvider;
import com.upgrad.quora.service.business.PasswordCryptographyProvider;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.common.QuoraConstants;
import com.upgrad.quora.service.dao.UserAuthRepository;
import com.upgrad.quora.service.dao.UserRepository;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.*;
import com.upgrad.quora.service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthRepository userAuthRepository;

    //private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Override
    public Response signUp(User request) throws SignUpRestrictedException {
        User user = userRepository.findUserByUserName(request.getUserName());
        if(null != user) {
            throw new SignUpRestrictedException(QuoraConstants.SGR001, QuoraConstants.USR_REG_DUP_USERNAME);
        }

        user = userRepository.findUserByEmailAddress(request.getEmailAddress());
        if(null != user) {
            throw new SignUpRestrictedException(QuoraConstants.SGR002, QuoraConstants.USR_REG_DUP_EMAIL);
        }

        request.setRole(QuoraConstants.NON_ADMIN);
        request.setPassword(String.valueOf(passwordCryptographyProvider.encrypt(request.getPassword())));
        user = userRepository.save(request);
        return new Response(user.getUuid(), QuoraConstants.USR_REG_SUCCESS);
    }

    @Override
    public User signIn(String credentials) throws Exception {
        String username = null;
        String encodedPwd = null;

        User user = userRepository.findUserByUserName(username);
        if(null == user) {
            throw new AuthenticationFailedException(QuoraConstants.ATH001, QuoraConstants.USRNM_NOT_FOUND);
        }
        if(!user.getPassword().equals(encodedPwd)) {
            throw new AuthenticationFailedException(QuoraConstants.ATH002, QuoraConstants.INCORRECT_PASSWORD);
        }

        final ZonedDateTime issuedDateTime = ZonedDateTime.now();
        final ZonedDateTime expiresDateTime = ZonedDateTime.now().plusMinutes(10);

        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encodedPwd);
        String token = jwtTokenProvider.generateToken(user.getUuid(), issuedDateTime, expiresDateTime);
        user.getUserAuth().setAccessToken(token);
        user.getUserAuth().setExpiresAt(expiresDateTime);
        user.getUserAuth().setLoginAt(issuedDateTime);
        user.getUserAuth().setUuid(user.getUuid());
        user = userRepository.save(user);

        return user;
    }

    @Override
    public Response signOut(String accessToken) throws SignOutRestrictedException {
        UserAuth userAuth = userAuthRepository.findUserAuthByAccessToken(accessToken);
        if(null == userAuth) {
            throw new SignOutRestrictedException(QuoraConstants.SGR001, QuoraConstants.USR_NOT_SIGNEDIN);
        }

        User user = userAuth.getUser();
        userAuth.setLogoutAt(ZonedDateTime.now());
        userAuthRepository.save(userAuth);
        return new Response(user.getUuid(), QuoraConstants.USR_SIGNOUT_SUCCESS);
    }

    @Override
    public Response getUserProfile(String userId, String token) throws Exception {
        UserAuth userAuth = userAuthRepository.findUserAuthByAccessToken(token);
        if(null == userAuth) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR001, QuoraConstants.USR_PRF_NOT_SIGNEDIN);
        }

        if(null != userAuth.getLogoutAt()) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR002, QuoraConstants.USR_PRF_SIGNEDOUT);
        }

        if(!userAuth.getUser().getUuid().equals(userId)) {
            throw new UserNotFoundException(QuoraConstants.USR001, QuoraConstants.USR_NOT_EXIST);
        }

        return new Response(userAuth.getUuid(), QuoraConstants.USR_PRF_SUCCESS);
    }

    @Override
    public Response deleteUser(String userId, String token) throws Exception {
        UserAuth userAuth = userAuthRepository.findUserAuthByAccessToken(token);
        if(null == userAuth) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR001, QuoraConstants.USR_PRF_NOT_SIGNEDIN);
        }
        if(null != userAuth.getLogoutAt()) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR002, QuoraConstants.USR_PRF_SIGNEDOUT);
        }
        if(QuoraConstants.NON_ADMIN.equals(userAuth.getUser().getRole())) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR003, QuoraConstants.USR_NOT_ADMIN);
        }

        User user = userRepository.findUserByUuid(userId);
        if(null == user) {
            throw new UserNotFoundException(QuoraConstants.USR001, QuoraConstants.USR_UUID_NOT_EXIST);
        }

        String uuid = user.getUuid();
        userRepository.delete(user);
        return new Response(uuid, QuoraConstants.USR_DELETE_SUCCESS);
    }
}
