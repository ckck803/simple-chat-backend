package org.example.simplechat.file.dto;

import lombok.Data;
import org.example.simplechat.file.entity.AttachFile;

@Data
public class SavedFileDto {
    String fileName;
    String fileDirectory;
    String fileType;
    String fileSize;

    public SavedFileDto(){}

    public SavedFileDto(AttachFile attachFile){
        this.fileName = attachFile.getSavedFileName();
        this.fileDirectory = attachFile.getFileDirectory();
        this.fileType = attachFile.getFileType();
        this.fileSize = attachFile.getFileSize();
    }
}
