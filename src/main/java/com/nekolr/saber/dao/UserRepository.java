package com.nekolr.saber.dao;

import com.nekolr.saber.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查询
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查询
     */
    User findByEmail(String email);


    /**
     * 通过用户名或者邮箱查询
     */
    User findByUsernameOrEmail(String username, String email);

}
