package com.sideby.user.mapper;

import com.sideby.user.entity.AppUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AppUserMapper {

    @Select("""
            SELECT
                id,
                username,
                email,
                password_hash,
                display_name,
                avatar_url,
                bio,
                status,
                created_at,
                updated_at
            FROM app_user
            WHERE id = #{id}
            LIMIT 1
            """)
    AppUser findById(@Param("id") Long id);

    @Select("""
            SELECT
                id,
                username,
                email,
                password_hash,
                display_name,
                avatar_url,
                bio,
                status,
                created_at,
                updated_at
            FROM app_user
            WHERE username = #{username}
            LIMIT 1
            """)
    AppUser findByUsername(@Param("username") String username);

    @Select("""
            SELECT
                id,
                username,
                email,
                password_hash,
                display_name,
                avatar_url,
                bio,
                status,
                created_at,
                updated_at
            FROM app_user
            WHERE email = #{email}
            LIMIT 1
            """)
    AppUser findByEmail(@Param("email") String email);

    @Select("""
            SELECT
                id,
                username,
                email,
                password_hash,
                display_name,
                avatar_url,
                bio,
                status,
                created_at,
                updated_at
            FROM app_user
            WHERE username = #{identifier}
               OR email = #{identifier}
            LIMIT 1
            """)
    AppUser findByIdentifier(@Param("identifier") String identifier);

    @Select("""
            SELECT COUNT(*)
            FROM app_user
            WHERE username = #{username}
            """)
    int countByUsername(@Param("username") String username);

    @Select("""
            SELECT COUNT(*)
            FROM app_user
            WHERE email = #{email}
            """)
    int countByEmail(@Param("email") String email);

    @Insert("""
            INSERT INTO app_user (
                username,
                email,
                password_hash,
                display_name
            )
            VALUES (
                #{username},
                #{email},
                #{passwordHash},
                #{displayName}
            )
            """)
    @Options(
        useGeneratedKeys = true,
        keyProperty = "id",
        keyColumn = "id"
    )
    int insert(AppUser user);
}
