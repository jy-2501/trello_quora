package com.upgrad.quora.service.business.impl;

import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.common.QuoraConstants;
import com.upgrad.quora.service.dao.AnswerRepository;
import com.upgrad.quora.service.dao.QuestionRepository;
import com.upgrad.quora.service.dao.UserAuthRepository;
import com.upgrad.quora.service.entity.Answer;
import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Override
    public Response createAnswer(String accessToken, String questionId, String answer) throws Exception {
        UserAuth userAuth = userAuthRepository.findUserAuthByAccessToken(accessToken);
        if(null == userAuth) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR001, QuoraConstants.USR_PRF_NOT_SIGNEDIN);
        }
        if(null != userAuth.getLogoutAt()) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR002, QuoraConstants.USR_PRF_SIGNEDOUT);
        }

        Question question = questionRepository.findQuestionByUuid(questionId);
        if(null == question) {
            throw new InvalidQuestionException(QuoraConstants.QUES001, QuoraConstants.QUES_INVALID);
        }

        Answer ans = new Answer();
        ans.setAnswer(answer);
        ans.setDate(ZonedDateTime.now());
        ans.setQuestionId(questionId);
        ans.setUuid(userAuth.getUuid());
        ans = answerRepository.save(ans);
        return new Response(ans.getUuid(), QuoraConstants.ANS_CREATED_SUCCESS);
    }

    @Override
    public Response editAnswerContent(String accessToken, String answerId, String content) throws Exception {
        UserAuth userAuth = userAuthRepository.findUserAuthByAccessToken(accessToken);
        if(null == userAuth) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR001, QuoraConstants.USR_PRF_NOT_SIGNEDIN);
        }
        if(null != userAuth.getLogoutAt()) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR002, QuoraConstants.USR_PRF_SIGNEDOUT);
        }

        Answer answer = answerRepository.findAnswerByUuid(answerId);
        if(null == answer) {
            throw new AnswerNotFoundException(QuoraConstants.ANS001, QuoraConstants.ANS_NOT_EXIST);
        }
        if(answer.getUserId() != userAuth.getUser().getId()) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR003, QuoraConstants.ANS_INVALID_OWNER);
        }

        answer.setAnswer(content);
        answer = answerRepository.save(answer);
        return new Response(answer.getUuid(), QuoraConstants.ANS_EDITED_SUCCESS);
    }

    @Override
    public Response deleteAnswer(String accessToken, String answerId) throws Exception {
        UserAuth userAuth = userAuthRepository.findUserAuthByAccessToken(accessToken);
        if(null == userAuth) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR001, QuoraConstants.USR_PRF_NOT_SIGNEDIN);
        }
        if(null != userAuth.getLogoutAt()) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR002, QuoraConstants.USR_PRF_SIGNEDOUT);
        }

        Answer answer = answerRepository.findAnswerByUuid(answerId);
        if(null == answer) {
            throw new AnswerNotFoundException(QuoraConstants.ANS001, QuoraConstants.ANS_NOT_EXIST);
        }
        if(QuoraConstants.NON_ADMIN.equals(userAuth.getUser().getRole()) && answer.getUserId() != userAuth.getUser().getId()) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR003, QuoraConstants.ANS_INVALID_NONADMIN_OWNER);
        }

        String uuid = answer.getUuid();
        answerRepository.delete(answer);
        return new Response(uuid, QuoraConstants.ANS_DELETED_SUCCESS);
    }

    @Override
    public Response getAllAnswersToQuestion(String accessToken, String questionId) throws Exception {
        UserAuth userAuth = userAuthRepository.findUserAuthByAccessToken(accessToken);
        if(null == userAuth) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR001, QuoraConstants.USR_PRF_NOT_SIGNEDIN);
        }
        if(null != userAuth.getLogoutAt()) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR002, QuoraConstants.USR_PRF_SIGNEDOUT);
        }

        Question question = questionRepository.findQuestionByUuid(questionId);
        if(null == question) {
            throw new InvalidQuestionException(QuoraConstants.QUES001, QuoraConstants.ANS_USR_UUID_NOT_EXIST);
        }

        List<Answer> answers = answerRepository.findAllByQuestionId(questionId);
        return new Response(userAuth.getUser().getUuid(), QuoraConstants.QSTN_LOADED_SUCCESS, null, answers);
    }


}
