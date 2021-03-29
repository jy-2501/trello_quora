package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Integer> {

    UserAuth findUserAuthByAccessToken(String accessToken);
}
