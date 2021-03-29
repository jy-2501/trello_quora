package com.upgrad.quora.service.business.impl;

import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.common.QuoraConstants;
import com.upgrad.quora.service.dao.QuestionRepository;
import com.upgrad.quora.service.dao.UserAuthRepository;
import com.upgrad.quora.service.dao.UserRepository;
import com.upgrad.quora.service.entity.Question;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import com.upgrad.quora.service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Response getAllQuestions(String accessToken) throws AuthorizationFailedException {
        UserAuth userAuth = userAuthRepository.findUserAuthByAccessToken(accessToken);
        if(null == userAuth) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR001, QuoraConstants.USR_PRF_NOT_SIGNEDIN);
        }
        if(null != userAuth.getLogoutAt()) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR002, QuoraConstants.USR_PRF_SIGNEDOUT);
        }

        List<Question> questions = questionRepository.findAll();
        return new Response(userAuth.getUser().getUuid(), QuoraConstants.QSTN_LOADED_SUCCESS, questions, null);
    }

    @Override
    public Response createQuestion(String accessToken, String content) throws AuthorizationFailedException {
        UserAuth userAuth = userAuthRepository.findUserAuthByAccessToken(accessToken);
        if(null == userAuth) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR001, QuoraConstants.USR_PRF_NOT_SIGNEDIN);
        }
        if(null != userAuth.getLogoutAt()) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR002, QuoraConstants.USR_PRF_SIGNEDOUT);
        }

        Question question = new Question();
        question.setContent(content);
        question.setDate(ZonedDateTime.now());
        question.setUuid(userAuth.getUuid());
        question.setUserId(userAuth.getUser().getId());

        question = questionRepository.save(question);

        return new Response(question.getUuid(), QuoraConstants.QSTN_CREATED_SUCCESS);
    }

    @Override
    public Response editQuestionContent(String accessToken, String questionId, String content) throws Exception {
        UserAuth userAuth = userAuthRepository.findUserAuthByAccessToken(accessToken);
        if(null == userAuth) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR001, QuoraConstants.USR_PRF_NOT_SIGNEDIN);
        }
        if(null != userAuth.getLogoutAt()) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR002, QuoraConstants.USR_PRF_SIGNEDOUT);
        }

        Question question = questionRepository.findQuestionByUuid(questionId);
        if(null == question) {
            throw new InvalidQuestionException(QuoraConstants.QUES001, QuoraConstants.QUES_NOT_EXIST);
        }
        if(question.getUserId() != userAuth.getUser().getId()) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR003, QuoraConstants.QUES_INVALID_OWNER);
        }

        question.setContent(content);
        question = questionRepository.save(question);
        return new Response(question.getUuid(), QuoraConstants.QSTN_EDITED_SUCCESS);
    }

    @Override
    public Response deleteQuestion(String accessToken, String questionId) throws Exception {
        UserAuth userAuth = userAuthRepository.findUserAuthByAccessToken(accessToken);
        if(null == userAuth) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR001, QuoraConstants.USR_PRF_NOT_SIGNEDIN);
        }
        if(null != userAuth.getLogoutAt()) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR002, QuoraConstants.USR_PRF_SIGNEDOUT);
        }

        Question question = questionRepository.findQuestionByUuid(questionId);
        if(null == question) {
            throw new InvalidQuestionException(QuoraConstants.QUES001, QuoraConstants.QUES_NOT_EXIST);
        }
        if(QuoraConstants.NON_ADMIN.equals(userAuth.getUser().getRole()) && question.getUserId() != userAuth.getUser().getId()) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR003, QuoraConstants.QUES_INVALID_NONADMIN_OWNER);
        }

        String uuid = question.getUuid();
        questionRepository.delete(question);
        return new Response(uuid, QuoraConstants.QSTN_DELETED_SUCCESS);
    }

    @Override
    public Response getAllQuestionsByUser(String accessToken, String userId) throws Exception {
        UserAuth userAuth = userAuthRepository.findUserAuthByAccessToken(accessToken);
        if(null == userAuth) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR001, QuoraConstants.USR_PRF_NOT_SIGNEDIN);
        }
        if(null != userAuth.getLogoutAt()) {
            throw new AuthorizationFailedException(QuoraConstants.ATHR002, QuoraConstants.USR_PRF_SIGNEDOUT);
        }

        User user = userRepository.findUserByUuid(userId);
        if(null == user) {
            throw new UserNotFoundException(QuoraConstants.USR001, QuoraConstants.QUES_USR_UUID_NOT_EXIST);
        }

        List<Question> questions = questionRepository.findAllByUserId(userId);
        return new Response(userAuth.getUser().getUuid(), QuoraConstants.QSTN_LOADED_SUCCESS, questions, null);
        /*return questions.stream()
                .map(question -> new Response(question.getUuid(), question.getContent()))
                .collect(Collectors.toList());*/
    }

}
