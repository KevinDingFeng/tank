package com.shenghesun.tank.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shenghesun.tank.service.entity.QuotedProduct;

@Service
public class QuotedProductService {

	@Autowired
	private QuotedProductDao quotedProductDao;

	public List<QuotedProduct> findByProductId(Long productId) {
		return quotedProductDao.findByProductId(productId);
	}

	public List<QuotedProduct> findByCoachId(Long coachId) {
		return quotedProductDao.findByCoachId(coachId);
	}

	public QuotedProduct findByCoachIdAndProductProductTypeCode(Long coachId, Integer code) {
		return quotedProductDao.findByCoachIdAndProductProductTypeCode(coachId, code);
	}

	public QuotedProduct findById(Long id) {
		return quotedProductDao.findOne(id);
	}

	public List<QuotedProduct> findByRemovedAndCoachIdAndProductProductTypeCodeIn(boolean bool, Long coachId, List<Integer> codes) {
		return quotedProductDao.findByRemovedAndCoachIdAndProductProductTypeCodeIn(bool, coachId, codes);
	}

	public List<QuotedProduct> findByRemovedAndCoachIdAndProductProductTypeParentCode(boolean b, Long coachId,
			int parentCode) {
		return quotedProductDao.findByRemovedAndCoachIdAndProductProductTypeParentCode(b, coachId, parentCode);
	}

	public List<QuotedProduct> findByRemovedAndCoachIdAndProductProductTypeIdIn(boolean b, Long coachId, List<Long> ids) {
		return quotedProductDao.findByRemovedAndCoachIdAndProductProductTypeIdIn(b, coachId, ids);
	}
	/**
	 * 计算总金额
	 * 	需要根据 具体报价 和 服务设定的时长计算出真实的金额
	 * @param duration 服务设定的时长
	 * @param price 具体报价的 单价
	 * @param qpDuration 具体报价对应服务的时长； 产品中存在有效的时长
	 * @return
	 */
	public BigDecimal getTotalFee(int duration, BigDecimal price, int qpDuration) {
		
		BigDecimal totalFee = BigDecimal.ZERO;
		if(price != null && price.compareTo(BigDecimal.ZERO) > 0) {
			if(qpDuration > 0) {
				//大神针对单个服务的具体报价的单价 X （选择服务的时长 / 服务的单位时长）
				totalFee = price.multiply(new BigDecimal(duration / qpDuration)); 
			}
		}
		return totalFee;
	}
	
	/**
	 * v2 
	 * @param duration
	 * @param price
	 * @param qpDuration
	 * @return
	 */
	public BigDecimal getTotalFeeV2(int duration, BigDecimal price) {
		
		BigDecimal totalFee = BigDecimal.ZERO;
		if(price != null && price.compareTo(BigDecimal.ZERO) > 0) {
				//大神针对单个服务的具体报价的单价 X （选择服务的时长 / 服务的单位时长）
			totalFee = price.multiply(new BigDecimal(duration)); 
		}
		return totalFee;
	}
	

	public QuotedProduct save(QuotedProduct entity) {
		return quotedProductDao.save(entity);
	}
	
	public List<QuotedProduct> findByRemovedAndCoachId(boolean removed,Long coachId){
		return quotedProductDao.findByRemovedAndCoachId(removed, coachId);
	}
	
	public Page<QuotedProduct> findMinPriceByGroup(Pageable pageable){
		return quotedProductDao.findGroupByCoach(pageable);
	}
	
}
