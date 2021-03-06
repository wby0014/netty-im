package com.wby.netty.server.im.server;

import com.wby.netty.server.im.common.ApplicationContextUtils;
import com.wby.netty.server.im.config.ConfigUtils;
import com.wby.netty.server.im.config.ServerProperties;
import com.wby.netty.server.im.handler.HeartBeatServerHandler;
import com.wby.netty.server.im.handler.NettyServletHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.net.InetSocketAddress;

/**
 * @Description
 * @Date 2020/9/7 17:18
 * @Author wuby31052
 */
@Component
public class ImNettyServer implements IServerBootstrap {

    private final static Logger log = LoggerFactory.getLogger(ImNettyServer.class);

    @Resource(name = "bossGroup")
    private NioEventLoopGroup bossGroup;

    @Resource(name = "workGroup")
    private NioEventLoopGroup workGroup;

    @Resource
    private ServerProperties serverProperties;

    private Channel channel;

    @Override
    public void startup() throws InterruptedException {
        log.info("自定义配置项：" + serverProperties.getIp());
        String[] protocols = StringUtils.commaDelimitedListToStringArray(ConfigUtils.get("netty.server.protocol"));
        String port;
        InetSocketAddress localAddress;
        ServerBootstrap serverBootstrap;

        for (String protocol : protocols) {
            port = ConfigUtils.get("netty." + protocol + ".port");
            Assert.notNull(port, protocol + "端口号未配置");
            localAddress = new InetSocketAddress(Integer.parseInt(port));
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).childHandler(
                    new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("decoder", (ChannelHandler) ApplicationContextUtils.getBean(protocol + "Decoder"));
                            pipeline.addLast("encoder", (ChannelHandler) ApplicationContextUtils.getBean(protocol + "Encoder"));
                            pipeline.addLast(ApplicationContextUtils.getBean(NettyServletHandler.class));
                            pipeline.addLast(ApplicationContextUtils.getBean(HeartBeatServerHandler.class));
                        }
                    }
            );
            ChannelFuture channelFuture = serverBootstrap.bind(localAddress).sync();
            channel = channelFuture.channel();
            String finalPort = port;
            channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
                if (channelFuture1.isSuccess()) {
                    log.info(String.format("[%s]协议启用端口[%s]成功", new Object[]{protocol, finalPort}));
                } else {
                    log.error(String.format("[%s]协议启用端口[%s]失败", new Object[]{protocol, finalPort}));
                }
            });
        }
    }


    @Override
    public void destroy() {
        log.info("destroy netty server ...");
        // 释放线程池资源
        if (channel != null) {
            channel.close();
        }
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
        log.info("destroy netty server complete.");
    }


}
