package org.example.simplechat.file.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AttachFileType {

    PROFILE("profile"), IMAGE("image"), FILE("file"), VIDEO("video"), THUMBNAIL("thumbnail");

    private final String value;
}
