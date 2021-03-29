package com.upgrad.quora.service.business;

import com.upgrad.quora.service.entity.Question;

import java.util.List;

public interface QuestionService {

    List<Question> getAllQuestions(String token);
}
