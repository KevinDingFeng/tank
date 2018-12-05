package com.shenghesun.tank.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.tank.service.entity.Product;
import com.shenghesun.tank.service.entity.ProductType;
import com.shenghesun.tank.service.entity.model.DurationType;

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
	
	@Transactional
	public Product initProduct(ProductType productType) {
		Product product = new Product();
		product.setProductType(productType);
		product.setProductTypeId(productType.getId());
		product.setCapacity(5);
		product.setDuration(0);
		product.setDurationType(DurationType.NoLimitation);
		
		return save(product);
	}
	
	/**
	 * 2018年11月30日10:52:05
	 * 
	 * v2版本
	 */
	public Product findByPtId(Long pTId) {
		return productDao.findByProductTypeId(pTId);
	}
	
	public List<Long> findByProductTypeIdIn(List<Long> ptIds){
		List<Product> list = productDao.findByProductTypeIdIn(ptIds);
		List<Long> pIds = new ArrayList<>(list.size());
		for (Product product : list) {
			pIds.add(product.getId());
		}
		return pIds;
	}

}
