package com.ahmeds.superdrive.mappers;

import com.ahmeds.superdrive.models.User;
import org.apache.ibatis.annotations.*;

@Mapper()
public interface UserMapper {

    @Select("select * from users where username=#{name}")
    User getUserByName(String name);

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES(#{username}, #{salt}, #{password}, #{firstname}, #{lastname})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(User user);

    @Delete("DELETE FROM USERS where lastname='Test'")
    int deleteTestUsers();
}
