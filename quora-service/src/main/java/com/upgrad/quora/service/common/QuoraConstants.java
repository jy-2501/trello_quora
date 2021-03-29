package com.upgrad.quora.service.common;

public class QuoraConstants {

    public static final String USR_REG_SUCCESS = "USER SUCCESSFULLY REGISTERED";
    public static final String USR_REG_DUP_USERNAME = "Try any other Username, this Username has already been taken";
    public static final String USR_REG_DUP_EMAIL = "This user has already been registered, try with any other emailId";

    public static final String USRNM_NOT_FOUND = "This username does not exist";
    public static final String INCORRECT_PASSWORD = "Password failed";

    public static final String USR_DELETE_SUCCESS = "USER SUCCESSFULLY DELETED";
    public static final String USR_SIGNIN_SUCCESS = "SIGNED IN SUCCESSFULLY";
    public static final String USR_SIGNOUT_SUCCESS = "SIGNED OUT SUCCESSFULLY";
    public static final String USR_NOT_SIGNEDIN = "User is not Signed in";

    public static final String USR_PRF_SUCCESS = "USER PROFILE SUCCESSFULLY LOADED";
    public static final String USR_PRF_NOT_SIGNEDIN = "User has not signed in";
    public static final String USR_PRF_SIGNEDOUT = "User is signed out.Sign in first to get user details";

    public static final String USR_NOT_EXIST = "User with entered uuid does not exist";
    public static final String USR_NOT_ADMIN = "Unauthorized Access, Entered user is not an admin";
    public static final String USR_UUID_NOT_EXIST = "User with entered uuid to be deleted does not exist";

    public static final String QSTN_CREATED_SUCCESS = "QUESTION CREATED";
    public static final String QSTN_EDITED_SUCCESS = "QUESTION EDITED";
    public static final String QSTN_DELETED_SUCCESS = "QUESTION DELETED";
    public static final String QSTN_LOADED_SUCCESS = "QUESTIONS SUCCESSFULLY LOADED";
    public static final String QUES_NOT_EXIST = "Entered question uuid does not exist";
    public static final String QUES_INVALID = "The question entered is invalid";
    public static final String QUES_INVALID_OWNER = "Only the question owner can edit the question";
    public static final String QUES_INVALID_NONADMIN_OWNER = "Only the question owner or admin can delete the question";
    public static final String QUES_USR_UUID_NOT_EXIST = "User with entered uuid whose question details are to be seen does not exist";

    public static final String ANS_CREATED_SUCCESS = "ANSWER CREATED";
    public static final String ANS_EDITED_SUCCESS = "ANSWER EDITED";
    public static final String ANS_DELETED_SUCCESS = "ANSWER DELETED";
    public static final String ANS_NOT_EXIST = "Entered answer uuid does not exist";
    public static final String ANS_INVALID_OWNER = "Only the answer owner can edit the answer";
    public static final String ANS_INVALID_NONADMIN_OWNER = "Only the answer owner or admin can delete the answer";
    public static final String ANS_USR_UUID_NOT_EXIST = "The question with entered uuid whose details are to be seen does not exist";

    public static final String NON_ADMIN = "nonadmin";

    public static final String SGR001 = "SGR-001";
    public static final String SGR002 = "SGR-002";

    public static final String ATH001 = "ATH-001";
    public static final String ATH002 = "ATH-002";

    public static final String ATHR001 = "ATHR-001";
    public static final String ATHR002 = "ATHR-002";
    public static final String ATHR003 = "ATHR-003";

    public static final String USR001 = "USR-001";
    public static final String USR002 = "USR-002";

    public static final String QUES001 = "QUES-001";
    public static final String ANS001 = "ANS-001";
}
