package com.shenghesun.tank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.tank.service.entity.Product;

@Service
public class ProductService {

	@Autowired
	private ProductDao productDao;

	public Product findById(Long id) {
		return productDao.findOne(id);
	}
}
