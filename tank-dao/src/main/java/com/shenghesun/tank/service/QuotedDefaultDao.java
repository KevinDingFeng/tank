package com.shenghesun.tank.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.tank.service.entity.QuotedDefault;

@Repository
public interface QuotedDefaultDao extends JpaRepository<QuotedDefault, Long>, JpaSpecificationExecutor<QuotedDefault> {

	Page<QuotedDefault> findByProductTypeCode(Integer code, Pageable pageable);

	QuotedDefault findByCoachIdAndProductTypeId(Long coachId, Long productTypeId);

}
