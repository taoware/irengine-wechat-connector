package com.irengine.wechat.connector.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.irengine.wechat.connector.SmsHelper;
import com.irengine.wechat.connector.WeChatConnector;
import com.irengine.wechat.connector.domain.Coupon;
import com.irengine.wechat.connector.service.UserService;

@Controller
public class ActivityController {
	
	private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);
	
	@Autowired
	private UserService userService; 

    @RequestMapping("/today")
    public String today(HttpServletRequest request, Model model) throws WxErrorException {
    	
		String code = request.getParameter("code");

		WxMpOAuth2AccessToken wxMpOAuth2AccessToken = WeChatConnector
				.getMpService().oauth2getAccessToken(code);
		WxMpUser wxMpUser = WeChatConnector.getMpService()
				.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
		
		String openId = wxMpUser.getOpenId();

        model.addAttribute("openid", openId);
        return "today";
    }
    
    @RequestMapping("/register")
    public String register(HttpServletRequest request, Model model) throws Exception {
    	
		String mobile = request.getParameter("username");
		String openId = request.getParameter("openid");
		
		logger.info("mobile: " + mobile + " openId: " + openId);
		
		if (!userService.verfiyMobileAndOpenId(mobile, openId))
			return "fail";
		else {
			Coupon coupon = userService.registerActivity(mobile, openId);
	        model.addAttribute("coupon", coupon.getCode());
	        return "succeed";
		}
    }

    @RequestMapping("/query")
    public String query(HttpServletRequest request, Model model) throws Exception {
    	
		String code = request.getParameter("code");

		WxMpOAuth2AccessToken wxMpOAuth2AccessToken = WeChatConnector
				.getMpService().oauth2getAccessToken(code);
		WxMpUser wxMpUser = WeChatConnector.getMpService()
				.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
		
		String openId = wxMpUser.getOpenId();
		
		Coupon coupon = userService.queryActivity(openId);
		if (null == coupon)
			return "fail";
		else {
	        model.addAttribute("coupon", coupon.getCode());
	        return "succeed";
		}
    }

    
    @RequestMapping("/notify")
    public void notify(@RequestParam("mobile") String mobile, @RequestParam("message") String message, HttpServletResponse response) throws IOException {
    	
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		
		String result = SmsHelper.send(mobile, message);

		response.getWriter().println(result);
    }

}
