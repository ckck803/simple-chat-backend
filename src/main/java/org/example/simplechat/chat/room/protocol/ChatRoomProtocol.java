package org.example.simplechat.chat.room.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomProtocol {

    String requestType;
    String roomId;
    String name;
}
