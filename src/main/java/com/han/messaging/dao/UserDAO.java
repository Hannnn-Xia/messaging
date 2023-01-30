package com.han.messaging.dao;

import com.han.messaging.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface UserDAO {

    @Insert("INSERT INTO user (username, nickname, password, register_time, gender, email, address, is_valid) " +
            "VALUES (#{username}, #{nickname}, #{password}, #{registerTime}, #{gender}, #{email}, #{address}, " +
            "#{isValid})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(User user);

    @Select("SELECT * FROM user WHERE username=#{username}")
    User selectByUsername(@Param("username") String username);

    @Select("SELECT * FROM user WHERE email = #{email}")
    User selectByEmail(@Param("email") String email);

    @Update("UPDATE user SET is_valid=1 WHERE id = #{id}")
    void updateToValid(@Param("id") Integer id);

    @Select("SELECT * FROM user WHERE login_token = #{loginToken}")
    User selectByLoginToken(@Param("loginToken") String loginToken);

    @Update("UPDATE user SET login_token = #{loginToken}, last_login_time = #{lastLoginTime} WHERE id = #{id}")
    void login(@Param("loginToken") String loginToken, @Param("lastLoginTime") Date date, @Param("id") Integer id);

    @Delete("DELETE FROM user")
    void deleteAll();

    @Select("SELECT * from user WHERE username LIKE #{q} OR email LIKE #{q} OR email LIKE #{q}")
    List<User> search(@Param("q") String q);

    @Select("SELECT * from user WHERE id = #{userId}")
    User selectByUserId(@Param("userId") Integer userId);
}

/*
public class UserDAOImpl implements UserDAO {
    void insert(User user) {
        Connection connection = connect(url, datasourceUser, datasourcePassword);
        connection.executeQuery(INSERT INTO user (username, nickname, password, register_time, gender, email,
        address, is_valid) " +
            "VALUES (?, ?, ?, ?, ?, ?", user.getUsername(),...);
        return;
    }
}
 */

// OOD
