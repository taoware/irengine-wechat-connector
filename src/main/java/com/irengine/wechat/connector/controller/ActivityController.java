package com.irengine.wechat.connector.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.irengine.wechat.connector.SmsHelper;
import com.irengine.wechat.connector.WeChatConnector;

@Controller
public class ActivityController {

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
    
    @RequestMapping("/notify")
    public void notify(@RequestParam("mobile") String mobile, @RequestParam("message") String message, HttpServletResponse response) throws IOException {
    	
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		
		String result = SmsHelper.send(mobile, message);

		response.getWriter().println(result);
    
    }

}
