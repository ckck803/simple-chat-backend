package org.example.simplechat.chat.controller;

import org.example.simplechat.chat.message.dto.ChatMessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.doNothing;

@SpringBootTest
class ChatMessageControllerTest {


    @Mock
    private ChatService chatService;

    private WebSocketStompClient stompClient;

    @BeforeEach
    public void setup() {
        this.stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        // this.stompClient = new WebSocketStompClient(new SockJsClient(
        //         Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))));
    }

    // @BeforeEach
    // public void setup() {
    //     this.stompClient = new WebSocketStompClient(new SockJsClient(
    //             Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))));
    //     this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    // }

    @Test
    void testSendChatMessage() throws Exception {
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        chatMessageDto.setMessage("Hello, World!");

        // doNothing().when(chatService).sendMessage(chatMessageDto);

        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.setDestination("/pub");

        StompSession session = stompClient.connectAsync("ws://localhost:8080/ws-stomp", new WebSocketHttpHeaders(), stompHeaders, new StompSessionHandlerAdapter() {
                })
                .get(10, TimeUnit.SECONDS);

        session.send(stompHeaders, chatMessageDto);

        // Mockito.verify(chatService, Mockito.times(1)).sendMessage(chatMessageDto);
    }

}