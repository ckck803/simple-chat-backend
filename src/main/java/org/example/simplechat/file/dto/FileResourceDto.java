package org.example.simplechat.file.dto;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class FileResourceDto {
    Resource resource;
    String contentType;
}
