package com.wby.netty.client.im.channelpool;

import com.wby.netty.client.im.handler.HeartBeatClientHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Date 2020/9/10 15:32
 * @Author wuby31052
 */
public class NettyChannelPoolHandler implements ChannelPoolHandler {

    private static final Logger logger = LoggerFactory.getLogger(NettyChannelPoolHandler.class);

    @Override
    public void channelReleased(Channel channel) throws Exception {
        logger.info("channelReleased Channel ID:" + channel.id());
    }

    @Override
    public void channelAcquired(Channel channel) throws Exception {
        logger.info("channelAcquired Channel ID:" + channel.id());
    }

    @Override
    public void channelCreated(Channel ch) throws Exception {
        ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
        logger.info("channelCreated. Channel ID: " + ch.id());
        SocketChannel channel = (SocketChannel) ch;
        channel.config().setKeepAlive(true);
        channel.config().setTcpNoDelay(true);
        channel.pipeline()
                .addLast(new DelimiterBasedFrameDecoder(1024, delimiter))
                .addLast(new StringDecoder()).addLast(new StringEncoder()).addLast(new HeartBeatClientHandler());
    }

}
