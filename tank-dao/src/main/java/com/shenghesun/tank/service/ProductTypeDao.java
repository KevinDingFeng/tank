package com.shenghesun.tank.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.tank.service.entity.ProductType;

@Repository
public interface ProductTypeDao extends JpaRepository<ProductType, Long>, JpaSpecificationExecutor<ProductType> {

	ProductType findByCode(int code);

	List<ProductType> findByParentCode(int code);

	List<ProductType> findByParentCodeIn(List<Integer> parentCodes);
	
}
