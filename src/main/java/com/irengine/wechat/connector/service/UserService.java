package com.irengine.wechat.connector.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.wechat.connector.domain.Coupon;
import com.irengine.wechat.connector.domain.User;
import com.irengine.wechat.connector.repository.CouponDao;
import com.irengine.wechat.connector.repository.UserDao;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private CouponDao couponDao;
	
	public boolean verfiyMobileAndOpenId(String mobile, String openId) {
		
		List<User> mobileUsers = userDao.findByMobile(mobile);
		List<User> openIdUsers = userDao.findByMobile(mobile);
		if (mobileUsers.size() > 0 || openIdUsers.size() > 0)
			return false;

		return true;
	}
	
	public Coupon registerActivity(String mobile, String openId) throws Exception {
		
		User user = new User(mobile, openId);
		
		List<Coupon> coupons = couponDao.findByCategoryAndStatus(1L, Coupon.STATUS.Unused); 
		if (0 == coupons.size()) throw new Exception("code unavailable");
		Coupon coupon = coupons.get(0);

		user.getCoupons().add(coupon);
		userDao.save(user);

		coupon.setStatus(Coupon.STATUS.Used);
		couponDao.save(coupon);

		return coupon;
	}
	
	public Coupon queryActivity(String openId) {
		
		List<User> users = userDao.findByOpenId(openId);
		
		if (users.size() > 0) {
			User user = users.get(0);
			if (null != user.getCoupons() && user.getCoupons().size() > 0)
				return user.getCoupons().get(0);
		}
		
		return null;
	}
}
