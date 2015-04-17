package com.irengine.wechat.connector;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;

public class WeChatConnector {

	  private static WxMpConfigStorage wxMpConfigStorage;
	  private static WxMpService wxMpService;
	  private static WxMpMessageRouter wxMpMessageRouter;
	  
	  public static WxMpConfigStorage getMpConfigStorage() {
		  return wxMpConfigStorage;
	  }
	  
	  public static WxMpService getMpService() {
		  return wxMpService;
	  }
	  
	  public static WxMpMessageRouter getMpMessageRouter() {
		  return wxMpMessageRouter;
	  }
	  
	  public static void init() throws IOException {
		    InputStream is = new ClassPathResource("wechat-connector.xml").getInputStream();
		    WeChatConnectorInMemoryConfigStorage config = WeChatConnectorInMemoryConfigStorage.fromXml(is);

		    wxMpConfigStorage = config;
		    wxMpService = new WxMpServiceImpl();
		    wxMpService.setWxMpConfigStorage(config);

//		    WxMpMessageHandler logHandler = new DemoLogHandler();
//		    WxMpMessageHandler textHandler = new DemoTextHandler();
//		    WxMpMessageHandler imageHandler = new DemoImageHandler();
//		    WxMpMessageHandler oauth2handler = new DemoOAuth2Handler();
//		    DemoGuessNumberHandler guessNumberHandler = new DemoGuessNumberHandler();
//
		    wxMpMessageRouter = new WxMpMessageRouter(wxMpService);
//		      wxMpMessageRouter
//		          .rule().handler(logHandler).next()
//		          .rule().msgType(WxConsts.XML_MSG_TEXT).matcher(guessNumberHandler).handler(guessNumberHandler).end()
//		          .rule().async(false).content("哈哈").handler(textHandler).end()
//		          .rule().async(false).content("图片").handler(imageHandler).end()
//		          .rule().async(false).content("oauth").handler(oauth2handler).end()
//		      ;
//
		  }
}
