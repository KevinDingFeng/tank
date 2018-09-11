package com.shenghesun.tank.sso.shiro.realization;

import java.util.Iterator;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.shenghesun.tank.sso.model.LoginInfo;
import com.shenghesun.tank.system.SysUserService;
import com.shenghesun.tank.system.entity.SysPermission;
import com.shenghesun.tank.system.entity.SysRole;
import com.shenghesun.tank.system.entity.SysUser;

/**
 * 认证和授权处理
 * 
 * @author kevin
 *
 */
public class CustomShiroRealm extends AuthorizingRealm {
	@Autowired
	private SysUserService sysUserService;


	/**
	 * 加入登录用户的角色和权限信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
		String account = (String) principals.getPrimaryPrincipal();
		SysUser sysUser = sysUserService.findByAccount(account);
		Set<SysRole> roles = sysUser.getRoles();
		if(roles != null && roles.size() > 0) {
			Iterator<SysRole> roleIt = roles.iterator();
			while (roleIt.hasNext()) {
				SysRole role = roleIt.next();
				authorizationInfo.addRole(role.getName());
				Set<SysPermission> perms = role.getPermissions();
				if(perms != null && perms.size() > 0) {
					Iterator<SysPermission> permIt = perms.iterator();
					while(permIt.hasNext()) {
						SysPermission perm = permIt.next();
						authorizationInfo.addStringPermission(perm.getName());
					}
				}
			}
		}
		return authorizationInfo;
	}

	/**
	 * 登录成功认证 salt 值，暂时使用用户的登录名，所以需要从数据库层级把登录名字段设置为唯一键；以后版本可以生成新的 salt 值，并对 salt
	 * 进行管理 10
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		System.out.println("获取登录者信息-->MyShiroRealm.doGetAuthenticationInfo()");
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
		String account = token.getUsername();
		SysUser sysUser = sysUserService.findByAccount(account);
		if(sysUser != null) {
			LoginInfo info = new LoginInfo(sysUser.getId(), sysUser.getName(), account);
			return new SimpleAuthenticationInfo(info, sysUser.getPassword(), getName());
		}
		return null;
	}

}
