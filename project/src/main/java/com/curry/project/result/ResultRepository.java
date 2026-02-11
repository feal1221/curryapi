package com.curry.project.result;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

//@Repository
public interface ResultRepository extends JpaRepository<ResultVo, String>, JpaSpecificationExecutor<ResultVo> {
}
