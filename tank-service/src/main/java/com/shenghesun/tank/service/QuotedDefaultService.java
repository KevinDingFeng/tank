package com.shenghesun.tank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shenghesun.tank.service.entity.QuotedDefault;

@Service
public class QuotedDefaultService {
	@Autowired
	private QuotedDefaultDao quotedDefaultDao;

	public Page<QuotedDefault> findByProductTypeCode(Integer code, Pageable pageable) {
		return quotedDefaultDao.findByProductTypeCode(code, pageable);
	}

	public List<QuotedDefault> findAll() {
		return quotedDefaultDao.findAll();
	}

	public QuotedDefault findCoachIdAndProductTypeId(Long coachId, Long productTypeId) {
		return quotedDefaultDao.findCoachIdAndProductTypeId(coachId, productTypeId);
	}
}
