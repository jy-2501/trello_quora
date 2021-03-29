package com.upgrad.quora.service.business;

import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.model.Response;

public interface UserService {

    Response signUp(User user) throws SignUpRestrictedException;

    User signIn(String credentials) throws Exception;

    Response signOut(String accessToken) throws SignOutRestrictedException;

    Response getUserProfile(String userId, String token) throws Exception;

    Response deleteUser(String userId, String token) throws Exception;
}
