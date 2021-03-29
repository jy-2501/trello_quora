package com.upgrad.quora.service.model;

import com.upgrad.quora.service.entity.Answer;
import com.upgrad.quora.service.entity.Question;

import java.util.List;

public class Response {

    private String code;
    private String status;
    private List<Question> questions;
    private List<Answer> answers;

    public Response(String code, String status) {
        this.code = code;
        this.status = status;
    }

    public Response(String code, String status, List<Question> questions, List<Answer> answers) {
        this.code = code;
        this.status = status;
        this.questions = questions;
        this.answers = answers;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
