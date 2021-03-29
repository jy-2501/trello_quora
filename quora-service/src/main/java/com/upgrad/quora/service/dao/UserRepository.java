package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByUserName(String userName);

    User findUserByEmailAddress(String emailAddress);

    User findUserByUuid(String uuid);

}
