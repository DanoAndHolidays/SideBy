package com.sideby.system.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SystemStatusMapper {

    @Select("SELECT 1")
    int checkDatabase();
}

