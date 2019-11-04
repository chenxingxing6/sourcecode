package com.demo.pubsub;

import redis.clients.jedis.JedisPubSub;

/**
 * User: lanxinghua
 * Date: 2019/11/4 16:29
 * Desc: 订阅监听器
 */
public class RedisMsgPubSubListener extends JedisPubSub {
    @Override
    public void unsubscribe() {
        super.unsubscribe();
    }

    @Override
    public void unsubscribe(String... channels) {
        super.unsubscribe(channels);
    }

    @Override
    public void subscribe(String... channels) {
        super.subscribe(channels);
    }

    @Override
    public void psubscribe(String... patterns) {
        super.psubscribe(patterns);
    }

    @Override
    public void punsubscribe() {
        super.punsubscribe();
    }

    @Override
    public void punsubscribe(String... patterns) {
        super.punsubscribe(patterns);
    }

    /**
     * 订阅频道
     * @param channel
     * @param subscribedChannels
     */
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        System.out.println("订阅频道:" + channel + " 订阅频道数量:" + subscribedChannels);
    }

    /**
     * 取消订阅频道
     * @param channel
     * @param subscribedChannels
     */
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println("取消订阅频道:" + channel + " 订阅频道数量:" + subscribedChannels);
    }

    /**
     * 监听订阅消息
     * @param channel
     * @param message
     */
    @Override
    public void onMessage(String channel, String message) {
        System.out.println("订阅频道:" + channel + " 消息 :" + message);
    }

    /**
     * 监听到订阅模式接受到消息时的回调
     * @param pattern
     * @param channel
     * @param message
     */
    @Override
    public void onPMessage(String pattern, String channel, String message) {
        System.out.println("onPMessage-pattern:" + pattern + " channel:" + channel + " message :" + message);
    }

    /**
     * 订阅频道模式时的回调
     * @param pattern
     * @param subscribedChannels
     */
    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        System.out.println("onPUnsubscribe-pattern:" + pattern + " is been subscribed:" + subscribedChannels);
    }

    /**
     * 取消订阅模式时的回调
     * @param pattern
     * @param subscribedChannels
     */
    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        System.out.println("onPUnsubscribe-pattern:" + pattern + " is been subscribed:" + subscribedChannels);
    }
}
