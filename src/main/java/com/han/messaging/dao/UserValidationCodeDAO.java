package com.han.messaging.dao;

import com.han.messaging.model.UserValidationCode;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserValidationCodeDAO {

    @Insert("INSERT INTO user_validation_code (user_id, validation_code) VALUES (#{userId}, #{validationCode})")
    void insert(UserValidationCode userValidationCode);

    @Select("SELECT * FROM user_validation_code WHERE user_id = #{userId} ORDER BY id desc LIMIT 1")
    UserValidationCode findLatestByUserId(@Param("userId") Integer userId);

    @Delete("DELETE FROM user_validation_code WHERE user_id = #{userId}")
    void deleteByUserId(@Param("userId") Integer userId);

    @Delete("DELETE FROM user_validation_code")
    void deleteAll();
}
