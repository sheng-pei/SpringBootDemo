package org.example.mybatisplus;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.type.JdbcType;
import org.example.mybatisplus.typehandler.BooleanTypeHandler;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@TableName(value = "aaa", autoResultMap = true)
public class BasePO {

    @TableId(
            value = "id",
            type = IdType.AUTO
    )
    private Long id;

    /**
     * 逻辑删除
     * 标记
     */
    @TableField(
            jdbcType = JdbcType.VARCHAR,
            value = "is_deleted",
            typeHandler = BooleanTypeHandler.class
    )
    private boolean deleted;

    @TableField(
            value = "name"
    )
    private String name;

    /**
     * 实体创建时间
     */
    @TableField(
            value = "gmt_create",
            fill = FieldFill.INSERT
    )
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8"
    )
    private Date gmtCreate;

    /**
     * 实体修改时间
     */
    @TableField(
            value = "gmt_modified",
            fill = FieldFill.INSERT_UPDATE
    )
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8"
    )
    private Date gmtModified;

    /**
     * 实体创建用户id
     */
    @TableField(
            value = "creator",
            fill = FieldFill.INSERT)
    private Long creator;
    /**
     * 实体修改用户id
     */
    @TableField(value = "modifier",
            fill = FieldFill.INSERT_UPDATE
    )
    private Long modifier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
