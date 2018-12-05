package com.shenghesun.tank.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shenghesun.tank.service.entity.QuotedProduct;

@Repository
public interface QuotedProductDao extends JpaRepository<QuotedProduct, Long>, JpaSpecificationExecutor<QuotedProduct> {

	List<QuotedProduct> findByProductId(Long productId);

	List<QuotedProduct> findByCoachId(Long coachId);

	QuotedProduct findByCoachIdAndProductProductTypeCode(Long coachId, Integer code);

	List<QuotedProduct> findByCoachIdAndProductProductTypeCodeIn(Long coachId, List<Integer> codes);
	

	List<QuotedProduct> findByRemovedAndCoachIdAndProductProductTypeCodeIn(boolean bool, Long coachId, List<Integer> codes);

	List<QuotedProduct> findByRemovedAndCoachIdAndProductProductTypeIdIn(boolean b, Long coachId, List<Long> ids);

	List<QuotedProduct> findByRemovedAndCoachIdAndProductProductTypeParentCode(boolean b, Long coachId, int parentCode);

	
	List<QuotedProduct> findByRemovedAndCoachId(boolean removed,Long coachId);
	
	/**
	 * v2 版本
	 */
	
	@Query("select qp from QuotedProduct qp where qp.price in(select min(price) from QuotedProduct group by qp.coach) order by qp.price asc ")
	Page<QuotedProduct> findGroupByCoach(Pageable pageable);
	
	List<QuotedProduct> findByProductIdInOrderByPriceAsc(List<Long> pIds);
	
}
