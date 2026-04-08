package com.curry.project.result;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<ResultVo, String>, JpaSpecificationExecutor<ResultVo> {
    List<ResultVo> findByCreatedTimeBetweenOrderByCreatedTimeAsc(OffsetDateTime start, OffsetDateTime end);
    List<ResultVo> findAllByOrderByCreatedTimeAsc();
}
