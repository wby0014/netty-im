package com.wby.netty.client.im.client;

import com.wby.netty.client.im.handler.HeartBeatClientHandler;
import com.wby.netty.client.im.handler.ImJsonClientDecoder;
import com.wby.netty.client.im.handler.ImJsonClientEncoder;
import com.wby.netty.client.im.message.ClientCallback;
import com.wby.netty.client.im.message.ClientRequest;
import com.wby.netty.client.im.message.ClientResponse;
import com.wby.netty.client.im.pool.ChannelPool;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @Description
 * @Date 2020/9/7 19:53
 * @Author wuby31052
 */
public class NettyImClient implements INettyImClient {

    private static final Logger log = LoggerFactory.getLogger(NettyImClient.class);

    protected String host;

    protected int port;

    protected Bootstrap bootstrap;

    protected int timeout = 0;

    protected int poolSize = 1;

    protected ChannelPool channelPool;

    protected ChannelHandler[] codecHandlers;


    public NettyImClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void startup() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast("decoder", new ImJsonClientDecoder());
                    pipeline.addLast("encoder", new ImJsonClientEncoder());
                    pipeline.addLast("handler", new HeartBeatClientHandler());
                }
            });
            ChannelFuture f = bootstrap.connect(new InetSocketAddress(host, port)).awaitUninterruptibly();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }

    @Override
    public void shutdown() {
    }

    @Override
    public <req extends ClientRequest, resp extends ClientResponse> resp send(req request) {
        return null;
    }

    @Override
    public void sendAysnc(ClientRequest request, ClientCallback callback) {

    }

    public static void main(String[] args) {
        NettyImClient client = new NettyImClient("127.0.0.1", 8830);
        client.startup();
    }

}
