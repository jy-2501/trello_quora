package com.upgrad.quora.service.business.impl;

import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.Question;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

//    @Autowired
//    private QuestionRepository questionRepository;

//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;

    @Override
    public List<Question> getAllQuestions(String token) {

        return null;//questionRepository.findAll();
    }
}
