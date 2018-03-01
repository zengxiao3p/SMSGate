package com.zx.sms.connect.manager.sgip;

import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zx.sms.codec.sgip12.codec.Sgip2CMPPBusinessHandler;
import com.zx.sms.connect.manager.EndpointEntity.ChannelType;
import com.zx.sms.connect.manager.EndpointManager;
import com.zx.sms.handler.api.BusinessHandlerInterface;
import com.zx.sms.handler.api.gate.SessionConnectedHandler;
import com.zx.sms.handler.api.smsbiz.MessageReceiveHandler;
import com.zx.sms.handler.sgip.SgipReportRequestMessageHandler;
import com.zx.sms.mbean.ConnState;
/**
 *经测试，35个连接，每个连接每200/s条消息
 *lenovoX250能承担7000/s消息编码解析无压力。
 *10000/s的消息服务不稳定，开个网页，或者打开其它程序导致系统抖动，会有大量消息延迟 (超过500ms)
 *
 *低负载时消息编码解码可控制在10ms以内。
 *
 */


public class TestSgipEndPoint {
	private static final Logger logger = LoggerFactory.getLogger(TestSgipEndPoint.class);

	@Test
	public void testSMPPEndpoint() throws Exception {
		ResourceLeakDetector.setLevel(Level.ADVANCED);
		final EndpointManager manager = EndpointManager.INS;

		SgipServerEndpointEntity server = new SgipServerEndpointEntity();
		server.setId("smppserver");
		server.setHost("127.0.0.1");
		server.setPort(8001);
		server.setValid(true);
		//使用ssl加密数据流
		server.setUseSSL(false);
		
		SgipServerChildEndpointEntity child = new SgipServerChildEndpointEntity();
		child.setId("smppchild");
		child.setLoginName("333");
		child.setLoginPassowrd("0555");

		child.setValid(true);
		child.setChannelType(ChannelType.DOWN);
		child.setMaxChannels((short)20);
		child.setRetryWaitTimeSec((short)30);
		child.setMaxRetryCnt((short)3);
		child.setReSendFailMsg(true);
		child.setIdleTimeSec((short)15);
//		child.setWriteLimit(200);
//		child.setReadLimit(200);
		List<BusinessHandlerInterface> serverhandlers = new ArrayList<BusinessHandlerInterface>();
		
		serverhandlers.add(new SgipReportRequestMessageHandler());
		serverhandlers.add(new Sgip2CMPPBusinessHandler());  //  将CMPP的对象转成SMPP对象，然后再经SMPP解码器处理
		serverhandlers.add(new MessageReceiveHandler());   // 复用CMPP的Handler
		child.setBusinessHandlerSet(serverhandlers);
		server.addchild(child);
		
		manager.addEndpointEntity(server);
		manager.openAll();
		
		SgipClientEndpointEntity client = new SgipClientEndpointEntity();
		client.setId("smppclient");
		client.setHost("127.0.0.1");
		client.setPort(8001);
		client.setLoginName("333");
		client.setLoginPassowrd("0555");
		client.setChannelType(ChannelType.DOWN);

		client.setMaxChannels((short)12);
		client.setRetryWaitTimeSec((short)100);
		client.setUseSSL(false);
//		client.setReSendFailMsg(true);
//		client.setWriteLimit(200);
//		client.setReadLimit(200);
		List<BusinessHandlerInterface> clienthandlers = new ArrayList<BusinessHandlerInterface>();
		clienthandlers.add(new Sgip2CMPPBusinessHandler()); //  将CMPP的对象转成SMPP对象，然后再经SMPP解码器处理
		clienthandlers.add(new SessionConnectedHandler(1000)); //// 复用CMPP的Handler
		client.setBusinessHandlerSet(clienthandlers);
		
		manager.openEndpoint(client);
		
		//LockSupport.park();
		 MBeanServer mserver = ManagementFactory.getPlatformMBeanServer();  

        ObjectName stat = new ObjectName("com.zx.sms:name=ConnState");
        mserver.registerMBean(new ConnState(), stat);
        System.out.println("start.....");
        
		Thread.sleep(300000);
		EndpointManager.INS.close();
	}
}
