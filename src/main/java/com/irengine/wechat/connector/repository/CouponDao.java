package com.irengine.wechat.connector.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.irengine.wechat.connector.domain.Coupon;
import com.irengine.wechat.connector.domain.Coupon.STATUS;

public interface CouponDao  extends CrudRepository<Coupon, Long> {

	public Coupon findOneByCode(String code);
	public List<Coupon> findByCategoryAndStatus(long category, STATUS status);
	
}
