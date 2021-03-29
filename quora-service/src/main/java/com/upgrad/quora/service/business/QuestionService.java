package com.upgrad.quora.service.business;

import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.model.Response;

public interface QuestionService {

    Response getAllQuestions(String accessToken) throws AuthorizationFailedException;

    Response createQuestion(String accessToken, String content) throws AuthorizationFailedException;

    Response editQuestionContent(String accessToken, String questionId, String content) throws Exception;

    Response deleteQuestion(String accessToken, String questionId) throws Exception;

    Response getAllQuestionsByUser(String accessToken, String userId) throws Exception;
}
