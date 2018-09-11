package com.shenghesun.tank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.tank.service.entity.QuotedProduct;

@Service
public class QuotedProductService {

	@Autowired
	private QuotedProductDao quotedProductDao;

	public List<QuotedProduct> findByProductId(Long productId) {
		return quotedProductDao.findByProductId(productId);
	}
}
