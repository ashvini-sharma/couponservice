package com.firstbeat.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.firstbeat.model.Coupon;

public interface CouponRepo extends JpaRepository<Coupon, Long> {
	Coupon findByCode(String code);

}
