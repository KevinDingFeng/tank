package com.shenghesun.tank.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.tank.order.entity.PlayOrder;
import com.shenghesun.tank.order.entity.PlayOrderPayRecord;
import com.shenghesun.tank.order.entity.PlayOrderPayRecord.PayStatus;

@Service
public class PlayOrderPayRecordService {

	@Autowired
	private PlayOrderPayRecordDao recordDao;

//	public PlayOrderPayRecord findByPlayOrderId(Long playOrderId) {
//		return recordDao.findByPlayOrderId(playOrderId);
//	}

	public PlayOrderPayRecord getDefaultRecord(PlayOrder playOrder) {
		PlayOrderPayRecord record = new PlayOrderPayRecord();
		record.setPlayOrder(playOrder);
		record.setPlayOrderId(playOrder.getId());
		
		record.setStatus(PayStatus.Complete);
//		record.setAmount(amount);
		record.setIntegralAmount(0L);
//		record.setChannel(PayChannel.Weixin);
//		record.setType(PayType.Cash);
//		record.setRemoved(false);
		return record;
	}

	public PlayOrderPayRecord save(PlayOrderPayRecord record) {
		return recordDao.save(record);
	}

	public PlayOrderPayRecord findByPrepayId(String prepayId) {
		return recordDao.findByPrepayId(prepayId);
	}
	
	public List<PlayOrderPayRecord> findByOrderId(Long playId){
		return recordDao.findByPlayOrderIdOrderByLastModifiedDesc(playId);
	}
	
}
