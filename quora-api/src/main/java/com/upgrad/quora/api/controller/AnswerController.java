package com.upgrad.quora.api.controller;

import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class AnswerController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @PostMapping("/question/{questionId}/answer/create")
    public ResponseEntity<Response> createAnswer (@RequestHeader("authorization") String accessToken,
                                                   @PathVariable("questionId") String questionId,
                                                   @RequestParam("answer") String answer) throws Exception {
        try {
            return new ResponseEntity<Response>(answerService.createAnswer(accessToken, questionId, answer), HttpStatus.OK);
        } catch (AuthorizationFailedException afe) {
            return new ResponseEntity<Response>(new Response(afe.getCode(), afe.getErrorMessage()), HttpStatus.FORBIDDEN);
        } catch (InvalidQuestionException iqe) {
            return new ResponseEntity<Response>(new Response(iqe.getCode(), iqe.getErrorMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/answer/edit/{answerId}")
    public ResponseEntity<Response> editAnswerContent(@RequestHeader("authorization") String accessToken,
                                                      @PathVariable("answerId") String answerId,
                                                      @RequestParam("content") String content) throws Exception {
        try {
            return new ResponseEntity<Response>(answerService.editAnswerContent(accessToken, answerId, content), HttpStatus.OK);
        } catch (AuthorizationFailedException afe) {
            return new ResponseEntity<Response>(new Response(afe.getCode(), afe.getErrorMessage()), HttpStatus.FORBIDDEN);
        } catch (AnswerNotFoundException anfe) {
            return new ResponseEntity<Response>(new Response(anfe.getCode(), anfe.getErrorMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/answer/delete/{answerId}")
    public ResponseEntity<Response> deleteAnswer(@RequestHeader("authorization") String accessToken,
                                                     @PathVariable("answerId") String answerId) throws Exception {
        try {
            return new ResponseEntity<Response>(answerService.deleteAnswer(accessToken, answerId), HttpStatus.OK);
        } catch (AuthorizationFailedException afe) {
            return new ResponseEntity<Response>(new Response(afe.getCode(), afe.getErrorMessage()), HttpStatus.FORBIDDEN);
        } catch (AnswerNotFoundException anfe) {
            return new ResponseEntity<Response>(new Response(anfe.getCode(), anfe.getErrorMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("answer/all/{questionId}")
    public ResponseEntity<Response> getAllAnswersToQuestion(@RequestHeader("authorization") String accessToken,
                                                            @PathVariable("questionId") String questionId) throws Exception {
        try {
            return new ResponseEntity<Response>(answerService.getAllAnswersToQuestion(accessToken, questionId), HttpStatus.OK);
        } catch (AuthorizationFailedException afe) {
            return new ResponseEntity<Response>(new Response(afe.getCode(), afe.getErrorMessage()), HttpStatus.FORBIDDEN);
        } catch (InvalidQuestionException iqe) {
            return new ResponseEntity<Response>(new Response(iqe.getCode(), iqe.getErrorMessage()), HttpStatus.NOT_FOUND);
        }
    }

}
