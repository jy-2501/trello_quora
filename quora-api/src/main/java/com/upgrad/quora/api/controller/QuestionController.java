package com.upgrad.quora.api.controller;

import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import com.upgrad.quora.service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/create")
    public ResponseEntity<Response> createQuestion(@RequestHeader("authorization") String accessToken,
                                                   @RequestParam("content") String content) {
        try {
            return new ResponseEntity<Response>(questionService.createQuestion(accessToken, content), HttpStatus.OK);
        } catch (AuthorizationFailedException afe) {
            return new ResponseEntity<Response>(new Response(afe.getCode(), afe.getErrorMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllQuestions(@RequestHeader("authorization") String accessToken) {
        try {
            return new ResponseEntity<Response>(questionService.getAllQuestions(accessToken), HttpStatus.OK);
        } catch (AuthorizationFailedException afe) {
            return new ResponseEntity<Response>(new Response(afe.getCode(), afe.getErrorMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/edit/{questionId}")
    public ResponseEntity<Response> editQuestionContent (@RequestHeader("authorization") String accessToken,
                                                         @PathVariable("questionId") String questionId,
                                                         @RequestParam("content") String content) throws Exception {
        try {
            return new ResponseEntity<Response>(questionService.editQuestionContent(accessToken, questionId, content), HttpStatus.OK);
        } catch (AuthorizationFailedException afe) {
            return new ResponseEntity<Response>(new Response(afe.getCode(), afe.getErrorMessage()), HttpStatus.FORBIDDEN);
        } catch (InvalidQuestionException afe) {
            return new ResponseEntity<Response>(new Response(afe.getCode(), afe.getErrorMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{questionId}")
    public ResponseEntity<Response> deleteQuestion  (@RequestHeader("authorization") String accessToken,
                                                         @PathVariable("questionId") String questionId) throws Exception {
        try {
            return new ResponseEntity<Response>(questionService.deleteQuestion(accessToken, questionId), HttpStatus.OK);
        } catch (AuthorizationFailedException afe) {
            return new ResponseEntity<Response>(new Response(afe.getCode(), afe.getErrorMessage()), HttpStatus.FORBIDDEN);
        } catch (InvalidQuestionException afe) {
            return new ResponseEntity<Response>(new Response(afe.getCode(), afe.getErrorMessage()), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/all/{userId}")
    public ResponseEntity<Response> getAllQuestionsByUser (@RequestHeader("authorization") String accessToken,
                                                                 @PathVariable("userId") String userId) throws Exception {
        try {
            return new ResponseEntity<Response>(questionService.getAllQuestionsByUser(accessToken, userId), HttpStatus.OK);
        } catch (AuthorizationFailedException afe) {
            return new ResponseEntity<Response>(new Response(afe.getCode(), afe.getErrorMessage()), HttpStatus.FORBIDDEN);
        } catch (UserNotFoundException unfe) {
            return new ResponseEntity<Response>(new Response(unfe.getCode(), unfe.getErrorMessage()), HttpStatus.NOT_FOUND);
        }
    }

}
