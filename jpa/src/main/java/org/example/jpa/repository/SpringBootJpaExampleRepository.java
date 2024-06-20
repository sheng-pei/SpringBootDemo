package org.example.jpa.repository;

import org.example.jpa.dto.DescriptionDto;
import org.example.jpa.po.SpringBootJpaExamplePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface SpringBootJpaExampleRepository extends JpaRepository<SpringBootJpaExamplePO, Long>, JpaSpecificationExecutor<SpringBootJpaExamplePO> {
    @Query("select new org.example.jpa.dto.DescriptionDto(id, description) from SpringBootJpaExamplePO where id = 1")
    DescriptionDto getDescription();
    SpringBootJpaExamplePO findFirstByDescriptionLike(String description);
}
