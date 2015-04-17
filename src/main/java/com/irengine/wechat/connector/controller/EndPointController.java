package com.irengine.wechat.connector.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxMenu;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.irengine.wechat.connector.WeChatConnector;

@Controller
public class EndPointController {
	
	private static Logger logger = LoggerFactory.getLogger(EndPointController.class);

    @RequestMapping("/")
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);

		String signature = request.getParameter("signature");
		String nonce = request.getParameter("nonce");
		String timestamp = request.getParameter("timestamp");

		if (!WeChatConnector.getMpService().checkSignature(timestamp, nonce,
				signature)) {
			// 消息签名不正确，说明不是公众平台发过来的消息
			response.getWriter().println("非法请求");
			return;
		}

		String echostr = request.getParameter("echostr");
		if (StringUtils.isNotBlank(echostr)) {
			// 说明是一个仅仅用来验证的请求，回显echostr
			response.getWriter().println(echostr);
			return;
		}

		String encryptType = StringUtils.isBlank(request
				.getParameter("encrypt_type")) ? "raw" : request
				.getParameter("encrypt_type");

		if ("raw".equals(encryptType)) {
			// 明文传输的消息
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request
					.getInputStream());
			WxMpXmlOutMessage outMessage = WeChatConnector.getMpMessageRouter()
					.route(inMessage);
			if (outMessage != null) {
				response.getWriter().write(outMessage.toXml());
			}
			return;
		}

		if ("aes".equals(encryptType)) {
			// 是aes加密的消息
			String msgSignature = request.getParameter("msg_signature");
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(
					request.getInputStream(),
					WeChatConnector.getMpConfigStorage(), timestamp, nonce,
					msgSignature);
			WxMpXmlOutMessage outMessage = WeChatConnector.getMpMessageRouter()
					.route(inMessage);
			response.getWriter().write(
					outMessage.toEncryptedXml(WeChatConnector
							.getMpConfigStorage()));
			return;
		}

		response.getWriter().println("不可识别的加密类型");
		return;
	}
    
    @RequestMapping("/menu")
    public void setupMenu(HttpServletResponse response) throws IOException {
    	
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
    	
        // setup menu
        InputStream isMenu = new ClassPathResource("wechat-connector-menu.json").getInputStream();

        try {
        	WeChatConnector.getMpService().menuDelete();
    		WxMenu menu = WxMenu.fromJson(isMenu);
    		WeChatConnector.getMpService().menuCreate(menu);
    		logger.info("create menu succeed.");
    	} catch (WxErrorException e) {
    		logger.error("create menu failed.");
    	}
        
        logger.info(WeChatConnector.getMpService().oauth2buildAuthorizationUrl(WxConsts.OAUTH2_SCOPE_BASE,null));
        
		response.getWriter().println("设置菜单");
    }
    
    @RequestMapping("/openid")
    public void getOpenId(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);

		String code = request.getParameter("code");
		try {
			response.getWriter().println("<h1>code</h1>");
			response.getWriter().println(code);

			WxMpOAuth2AccessToken wxMpOAuth2AccessToken = WeChatConnector
					.getMpService().oauth2getAccessToken(code);
			WxMpUser wxMpUser = WeChatConnector.getMpService()
					.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
			response.getWriter().println("<h1>user open id</h1>");
			response.getWriter().println(wxMpUser.getOpenId());

		} catch (WxErrorException e) {
			e.printStackTrace();
		}

		response.getWriter().flush();
		response.getWriter().close();
    }

}
