package com.nekolr.saber.dao;

import com.nekolr.saber.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor {

    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查询
     *
     * @param email
     * @return
     */
    User findByEmail(String email);


    /**
     * 通过用户名或者邮箱查询
     *
     * @param username
     * @param email
     * @return
     */
    User findByUsernameOrEmail(String username, String email);

}
