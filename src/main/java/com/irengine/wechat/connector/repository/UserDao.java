package com.irengine.wechat.connector.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.irengine.wechat.connector.domain.User;

public interface UserDao  extends CrudRepository<User, Long> {
	
	List<User> findByMobile(String mobile);
	List<User> findByOpenId(String openId);
	
}
