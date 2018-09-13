package com.shenghesun.tank.system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.tank.system.entity.SysPool;


@Repository
public interface SysPoolDao extends JpaRepository<SysPool, Long>, JpaSpecificationExecutor<SysPool> {

}
