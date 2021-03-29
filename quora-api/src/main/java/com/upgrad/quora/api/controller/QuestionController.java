package com.upgrad.quora.api.controller;

import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/all")
    public ResponseEntity<List<Question>> getAllQuestions(@RequestHeader("authorization") String token) {
        questionService.getAllQuestions(token);
        return ResponseEntity.ok(questionService.getAllQuestions(token));
    }

}
