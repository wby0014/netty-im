package com.wby.netty.client.im.objectpool;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPoolFactory;

/**
 * @Description
 * @Date 2020/9/10 14:14
 * @Author wuby31052
 */
public class DefaultChannelPool implements ChannelPool {

    private ObjectPool channelObjectPool;

    public DefaultChannelPool(final ChannelGroup poolAbleChannelGroup) {
        this.channelObjectPool = new GenericObjectPoolFactory(new BasePoolableObjectFactory() {
            private ChannelGroup pooledChannelGroup = new DefaultChannelGroup(null);

            @Override
            public Channel makeObject() throws Exception {
                for (Channel poolableChannel : poolAbleChannelGroup) {
                    if (!pooledChannelGroup.contains(poolableChannel)) {
                        pooledChannelGroup.add(poolableChannel);
                        return poolableChannel;
                    }
                }
                throw new UnsupportedOperationException("没有更多的 Channel 可用！");
            }
        }, poolAbleChannelGroup.size(), GenericObjectPool.DEFAULT_WHEN_EXHAUSTED_ACTION, 5000).createPool();
    }

    @Override
    public Channel borrowChannel() throws Exception {
        return (Channel) channelObjectPool.borrowObject();
    }

    /**
     * 返回channel
     *
     * @param channel
     * @throws Exception
     */
    @Override
    public void returnChannel(Channel channel) throws Exception {
        this.channelObjectPool.returnObject(channel);
    }

}
