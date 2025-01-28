package org.example.simplechat.chat.room.protocol;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatRoomProtocolType {
    CREATE("CREATE"), DELETE("DELETE");

    private final String value;
}
