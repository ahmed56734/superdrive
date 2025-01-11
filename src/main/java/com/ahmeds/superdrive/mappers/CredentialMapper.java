package com.ahmeds.superdrive.mappers;

import com.ahmeds.superdrive.models.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<Credential> getCredentialsByUser(Integer userId);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    Credential getCredentialById(Integer credentialId);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) " +
            "VALUES(#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insert(Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, " +
            "key = #{key}, password = #{password} WHERE credentialid = #{credentialId}")
    void update(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    void delete(Integer credentialId);
}
