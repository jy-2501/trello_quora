package com.upgrad.quora.api.controller;

import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.common.QuoraConstants;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Response> singUp(@Valid User user) {
        try {
            return new ResponseEntity<Response>(userService.signUp(user), HttpStatus.CREATED);
        } catch (SignUpRestrictedException sre) {
            return new ResponseEntity<Response>(new Response(sre.getCode(), sre.getErrorMessage()), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<Response> singIn(@RequestHeader("authorization") String credentials) throws Exception {
        User user = userService.signIn(credentials);

        HttpHeaders headers = new HttpHeaders();
        headers.set("access_token", user.getUserAuth().getAccessToken());

        return new ResponseEntity<Response>(new Response(user.getUuid(), QuoraConstants.USR_SIGNIN_SUCCESS),
                headers, HttpStatus.OK);
    }

    @PostMapping("/signout")
    public ResponseEntity<Response> singOut(@RequestHeader("authorization") String accessToken) throws Exception {
        try {
            return new ResponseEntity<Response>(userService.signOut(accessToken), HttpStatus.OK);
        } catch (SignOutRestrictedException sre) {
            return new ResponseEntity<Response>(new Response(sre.getCode(), sre.getErrorMessage()), HttpStatus.UNAUTHORIZED);
        }

    }


}
