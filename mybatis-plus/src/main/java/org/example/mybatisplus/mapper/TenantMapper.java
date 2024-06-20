package org.example.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.example.mybatisplus.BasePO;
import org.example.mybatisplus.TenantPO;

import java.util.Collection;
import java.util.List;

public interface TenantMapper extends BaseMapper<TenantPO> {
    Page<TenantPO> pageByTenantNameLikeAndProductCode(
            @Param("page") Page<TenantPO> page,
            @Param("tenantName") String tenantName,
            @Param("codes") Collection<String> codes,
            @Param("sortField") String sortField,
            @Param("sort") String sort);

    void batchInsert(@Param("aaas") List<BasePO> aaas);

    void insert(@Param("id") Long id, @Param("name") String name, @Param("is_deleted") Boolean deleted);
}
