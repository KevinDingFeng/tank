package com.shenghesun.tank.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.tank.service.entity.Product;

@Repository
public interface ProductDao extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

	/**
	 * 2018年11月30日10:51:51
	 * v2 版本
	 * @param ptId
	 * @return
	 */
	Product findByProductTypeId(Long ptId);
	
	List<Product> findByProductTypeIdIn(List<Long> ptIds);
	
}
