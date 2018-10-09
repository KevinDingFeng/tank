package com.shenghesun.tank.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.tank.service.entity.QuotedProduct;

@Repository
public interface QuotedProductDao extends JpaRepository<QuotedProduct, Long>, JpaSpecificationExecutor<QuotedProduct> {

	List<QuotedProduct> findByProductId(Long productId);

	List<QuotedProduct> findByCoachId(Long coachId);

	QuotedProduct findByCoachIdAndProductProductTypeCode(Long coachId, Integer code);

	List<QuotedProduct> findByCoachIdAndProductProductTypeCodeIn(Long coachId, List<Integer> codes);
	

	List<QuotedProduct> findByRemovedAndCoachIdAndProductProductTypeCodeIn(boolean bool, Long coachId, List<Integer> codes);

}
