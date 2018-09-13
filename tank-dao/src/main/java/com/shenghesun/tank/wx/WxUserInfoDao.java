package com.shenghesun.tank.wx;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.tank.wx.entity.WxUserInfo;


@Repository
public interface WxUserInfoDao extends JpaRepository<WxUserInfo, Long>, JpaSpecificationExecutor<WxUserInfo> {

	WxUserInfo findByOpenId(String openId);

}
