package com.wby.netty.client.im.client;

import com.wby.netty.client.im.channelpool.NettyChannelPoolHandler;
import com.wby.netty.client.im.handler.HeartBeatClientHandler;
import com.wby.netty.client.im.handler.ImJsonClientDecoder;
import com.wby.netty.client.im.handler.ImJsonClientEncoder;
import com.wby.netty.client.im.message.ClientCallback;
import com.wby.netty.client.im.message.ClientRequest;
import com.wby.netty.client.im.message.ClientResponse;
import com.wby.netty.client.im.objectpool.ChannelPool;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @Description IM客户端
 * @Date 2020/9/7 19:53
 * @Author wuby31052
 */
public class NettyImClient extends SimpleChannelInboundHandler<ClientResponse> implements INettyImClient {

    private static final Logger log = LoggerFactory.getLogger(NettyImClient.class);

    private InetSocketAddress inetSocketAddress;

    protected Bootstrap bootstrap;

    private ClientResponse response;

    private ChannelPool channelPool;

    ChannelPoolMap<InetSocketAddress, SimpleChannelPool> poolMap;

    public NettyImClient(String host, int port) {
        inetSocketAddress = new InetSocketAddress(host, port);
    }

    public static void main(String[] args) {
        NettyImClient client = new NettyImClient("127.0.0.1", 8830);
        client.startup();
        ClientRequest request = new ClientRequest();
        request.setId("12345678");
        for (int i = 0; i < 5; i++) {
           client.send(request);
        }
    }

    @Override
    public void startup() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true);
           /* bootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast("decoder", new ImJsonClientDecoder());
                    pipeline.addLast("encoder", new ImJsonClientEncoder());
                    pipeline.addLast("handler", new HeartBeatClientHandler());
                }
            });*/
            poolMap = new AbstractChannelPoolMap<InetSocketAddress, SimpleChannelPool>() {
                @Override
                protected SimpleChannelPool newPool(InetSocketAddress inetSocketAddress) {
                    return new FixedChannelPool(bootstrap.remoteAddress(inetSocketAddress), new NettyChannelPoolHandler(), 2);
                }
            };
//            Channel channel = bootstrap.connect(new InetSocketAddress(host, port)).awaitUninterruptibly().channel();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            group.shutdownGracefully();
        }
    }

    @Override
    public void shutdown() {
    }

    @Override
    public <req extends ClientRequest, resp extends ClientResponse> resp send(req request) {
        // 写入 RPC 请求数据并关闭连接
        SimpleChannelPool pool = poolMap.get(inetSocketAddress);
        Future<Channel> future = pool.acquire();
        future.addListener((FutureListener<Channel>) f1 -> {
            if (f1.isSuccess()) {
                log.info("get channel success");
                Channel ch = f1.getNow();
                ch.writeAndFlush("request");
                pool.release(ch);
            } else {
                log.error("get channel error");
            }
        });
        return (resp) response;
    }

    @Override
    public void sendAysnc(ClientRequest request, ClientCallback callback) {

    }

    /**
     * 通过自定义对象池的方式
     *
     * @param request
     * @return
     */
    private ClientResponse sendByObjectPool(ClientRequest request) {
        Channel channel = null;
        try {
            channel = channelPool.borrowChannel();
            channel.writeAndFlush(request).sync();
            channelPool.returnChannel(channel);
        } catch (Exception e) {
            e.printStackTrace();
            if (channel != null) {
                try {
                    channelPool.returnChannel(channel);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        // 返回 RPC 响应对象
        return response;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ClientResponse clientResponse) throws Exception {
        this.response = clientResponse;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("client caught exception", cause);
        ctx.close();
    }
}
