package com.shenghesun.tank.service;

import java.util.List;

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

	public Product save(Product entity) {
		return productDao.save(entity);
	}
	
	public List<Product> findAll(){
		return productDao.findAll();
	}
}
