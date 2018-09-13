package com.shenghesun.tank.system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.tank.system.entity.SysUser;
@Repository
public interface SysUserDao extends JpaRepository<SysUser, Long>, JpaSpecificationExecutor<SysUser> {

	SysUser findByAccount(String account);

	SysUser findByOpenId(String openId);
}
