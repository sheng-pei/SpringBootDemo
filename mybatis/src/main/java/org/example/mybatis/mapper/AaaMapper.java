package org.example.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.mybatis.entity.Aaa;

import java.util.List;

@Mapper
public interface AaaMapper {
    @Insert("insert into aaa (id, name, is_deleted) values (#{aaa.id}, #{aaa.name}, #{aaa.deleted})")
    void insert(@Param("aaa") Aaa aaa);
    void batchInsert(@Param("aaas") List<Aaa> aaas);
    int uuuu(@Param("id") Long id, @Param("name") String name);
}
