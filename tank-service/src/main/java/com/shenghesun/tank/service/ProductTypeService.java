package com.shenghesun.tank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.tank.service.entity.ProductType;

@Service
public class ProductTypeService {

	@Autowired
	private ProductTypeDao productTypeDao;

	public ProductType findByCode(Integer code) {
		return productTypeDao.findByCode(code);
	}

	public List<ProductType> findByParentCode(Integer code) {
		return productTypeDao.findByParentCode(code);
	}

	public List<ProductType> findByParentCodeIn(List<Integer> parentCodes) {
		return productTypeDao.findByParentCodeIn(parentCodes);
	}

	public List<ProductType> findAll() {
		return productTypeDao.findAll();
	}
}
