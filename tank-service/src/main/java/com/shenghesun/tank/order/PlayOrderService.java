package com.shenghesun.tank.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.shenghesun.tank.order.entity.PlayOrder;

@Service
public class PlayOrderService {

	@Autowired
	private PlayOrderDao playOrderDao;

	public Page<PlayOrder>  findBySpecification(Specification<PlayOrder> spec, Pageable pageable) {
		return playOrderDao.findAll(spec, pageable);
	}

	public PlayOrder findById(Long id) {
		return playOrderDao.findOne(id);
	}

	public PlayOrder save(PlayOrder entity) {
		return playOrderDao.save(entity);
	}

	public Page<PlayOrder> findByWxUserId(Long wxUserId, Pageable pageable) {
		return playOrderDao.findByWxUserId(wxUserId, pageable);
	}

	public PlayOrder findMainByNo(String no) {
		List<PlayOrder> list = this.findByNoAndMain(no, true);
		if(list != null) {
			return list.get(0);
		}
		return null;
	}
	public List<PlayOrder> findByNoAndMain(String no, boolean bool){
		return playOrderDao.findByNoAndMain(no, true);
	}
}
