package com.irengine.wechat.connector.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ss_user")
public class User extends EntityBase {
	
	private String mobile;
	private String openId;
	private List<Coupon> coupons;
	
	public User() {
		
	}
	
	public User(String mobile, String openId) {
		this.mobile = mobile;
		this.openId = openId;
		this.coupons = new ArrayList<Coupon>();
	}

	@Column(nullable = false, unique=true)	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(nullable = false, unique=true)	
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

    @ManyToMany
    @JoinTable(name="ss_user_coupon",
          joinColumns=@JoinColumn(name="user_id"),
          inverseJoinColumns=@JoinColumn(name="coupon_id"))
	public List<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

}
