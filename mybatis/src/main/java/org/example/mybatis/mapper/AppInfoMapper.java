package org.example.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.mybatis.entity.AppInfo;

/**
 * @Title: AppInfoMapper
 * @Description:
 * @author: wuwei
 * @date: 2022/4/28 15:32
 */
@Mapper
public interface AppInfoMapper {
    AppInfo selectOne(@Param("id") Long id);
}
