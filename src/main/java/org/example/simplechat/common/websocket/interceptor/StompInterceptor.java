package org.example.simplechat.common.websocket.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Component
public class StompInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);

        if(Objects.equals(stompHeaderAccessor.getCommand(), StompCommand.CONNECT)){

        }

        if(stompHeaderAccessor.getCommand().equals(StompCommand.SUBSCRIBE)){

        }

        if(stompHeaderAccessor.getCommand().equals(StompCommand.DISCONNECT)){

        }

        return ChannelInterceptor.super.preSend(message, channel);
    }
}
