package com.shenghesun.tank.service;

import java.util.ArrayList;
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
	/**
	 * 根据 level 1 的 code 获取 level 3 级的 code 集合
	 * @param codeLevel1
	 * @return
	 */
	public List<Integer> getLevel3CodeList(int codeLevel1) {
		List<ProductType> typeLevel2List = this.findByParentCode(codeLevel1);
		List<Integer> typeLevel2IdList = this.getCodes(typeLevel2List);
		List<ProductType> typeLevel3List = this.findByParentCodeIn(typeLevel2IdList);
		return this.getCodes(typeLevel3List);
	}
	private List<Integer> getCodes(List<ProductType> entityList) {
		List<Integer> codeList = new ArrayList<>();
		if (entityList != null && entityList.size() > 0) {
			for (ProductType pt : entityList) {
				codeList.add(pt.getCode());
			}
		}
		return codeList;
	}
	/**
	 * 根据 level 1 的 code 获取 level 3 级的 id 集合
	 * @param codeLevel1
	 * @return
	 */
	public List<Long> getLevel3IdList(int codeLevel1) {
		List<ProductType> typeLevel2List = this.findByParentCode(codeLevel1);
		List<Integer> typeLevel2IdList = this.getCodes(typeLevel2List);
		List<ProductType> typeLevel3List = this.findByParentCodeIn(typeLevel2IdList);
		return this.getIds(typeLevel3List);
	}
	private List<Long> getIds(List<ProductType> entityList) {
		List<Long> codeList = new ArrayList<>();
		if (entityList != null && entityList.size() > 0) {
			for (ProductType pt : entityList) {
				codeList.add(pt.getId());
			}
		}
		return codeList;
	}
	
	public Integer getLevel2CodeByLevel3Code(Integer level3Code) {
		//TODO 暂时的业务逻辑中使用不到
		return null;
	}
	/**
	 * 计算逻辑，见实体 ProductType 中的说明
	 * 	level 3 code = 两位 level 1 code + 两位 level 2 code + 两位 level 3 code
	 * @param level3Code
	 * @return
	 */
	public Integer getLevel1CodeByLevel3Code(Integer level3Code) {
		if(level3Code == null || level3Code < 100000) {
			return null;
		}
		return level3Code / 10000;
	}
	
	public Integer getLevel1CodeByLevel2Code(Integer level2Code) {
		//TODO 暂时的业务逻辑中使用不到
		return null;		
	}
	
}
