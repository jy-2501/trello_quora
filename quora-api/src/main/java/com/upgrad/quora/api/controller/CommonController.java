package com.upgrad.quora.api.controller;

import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import com.upgrad.quora.service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userprofile")
public class CommonController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<Response> getUserProfile(@RequestHeader("authorization") String token,
                                                   @PathVariable("userId") String userId) throws Exception {
        try {
            return new ResponseEntity<Response>(userService.getUserProfile(userId, token), HttpStatus.OK);
        } catch (AuthorizationFailedException afe) {
            return new ResponseEntity<Response>(new Response(afe.getCode(), afe.getErrorMessage()), HttpStatus.FORBIDDEN);
        } catch (UserNotFoundException unfe) {
            return new ResponseEntity<Response>(new Response(unfe.getCode(), unfe.getErrorMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
