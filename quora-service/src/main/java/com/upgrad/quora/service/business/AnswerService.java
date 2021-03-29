package com.upgrad.quora.service.business;

import com.upgrad.quora.service.model.Response;

public interface AnswerService {

    Response createAnswer(String accessToken, String questionId, String answer) throws Exception;

    Response editAnswerContent(String accessToken, String answerId, String content) throws Exception;

    Response deleteAnswer(String accessToken, String answerId) throws Exception;

    Response getAllAnswersToQuestion(String accessToken, String questionId) throws Exception;

}
