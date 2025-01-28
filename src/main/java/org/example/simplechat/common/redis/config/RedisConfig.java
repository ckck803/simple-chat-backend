package org.example.simplechat.common.redis.config;

import io.lettuce.core.pubsub.RedisPubSubListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simplechat.chat.message.subscribe.ChatMessageSubscriber;
import org.example.simplechat.chat.room.pubsub.ChatRoomSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisConnectionFactory redisConnectionFactory;
    private final ChatMessageSubscriber chatMessageSubscriber;
    private final ChatRoomSubscriber chatRoomSubscriber;

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        log.info("Create RedisTemplate");

        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));

        return redisTemplate;
    }

    @Bean
    public ChannelTopic simpleChatTopic(){
        return new ChannelTopic("simple-chat");
    }

    @Bean
    public MessageListenerAdapter simpleChatTopicListerAdaptor(){
        return new MessageListenerAdapter(chatMessageSubscriber, "sendToUser");
    }

    @Bean
    public ChannelTopic simpleChatRoomTopic(){
        return new ChannelTopic("simple-chat-room");
    }

    @Bean
    public MessageListenerAdapter simpleChatRoomListerAdapter(){
        return new MessageListenerAdapter(chatRoomSubscriber, "sendToAllUser");
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(){
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.addMessageListener(simpleChatTopicListerAdaptor(),simpleChatTopic());
        redisMessageListenerContainer.addMessageListener(simpleChatRoomListerAdapter(),simpleChatRoomTopic());
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);

        return redisMessageListenerContainer;
    }
}
