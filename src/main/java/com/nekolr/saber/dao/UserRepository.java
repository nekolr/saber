package com.nekolr.saber.dao;

import com.nekolr.saber.entity.User;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@NullMarked
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查询
     */

    @Nullable User findByUsername(String username);

    /**
     * 根据邮箱查询
     */
    @Nullable User findByEmail(String email);


    /**
     * 通过用户名或者邮箱查询
     */
    @Nullable User findByUsernameOrEmail(String username, String email);

}
