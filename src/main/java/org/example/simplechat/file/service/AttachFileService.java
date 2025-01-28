package org.example.simplechat.file.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simplechat.chat.message.dto.ChatMessageDto;
import org.example.simplechat.chat.message.entity.ChatMessage;
import org.example.simplechat.chat.message.service.ChatMessageService;
import org.example.simplechat.chat.message.subscribe.ChatMessageSubscriber;
import org.example.simplechat.file.constant.AttachFileType;
import org.example.simplechat.file.dto.SavedFileDto;
import org.example.simplechat.file.entity.AttachFile;
import org.example.simplechat.file.repository.AttachFileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttachFileService {

    private final AttachFileRepository attachFileRepository;
    private final ChatMessageService chatMessageService;
    private final ChatMessageSubscriber subscriber;
    private final ObjectMapper objectMapper;

    @Value("${file.path}")
    private String savedFilePath;

    // public List<SavedFileDto> saveAttachFile(MultipartFile[] multipartFiles, String fileType) throws IOException {
    //
    //     List<SavedFileDto> savedFileDtoList = new ArrayList<>();
    //     for (MultipartFile multipartFile : multipartFiles) {
    //         savedFileDtoList.add(getAttachFile(multipartFile, fileType));
    //     }
    //     return savedFileDtoList;
    // }

    // public List<SavedFileDto> saveAttachFile(MultipartFile[] multipartFiles, String fileType, ChatMessageDto chatMessageDto) throws IOException {
    //
    //     List<SavedFileDto> savedFileDtoList = new ArrayList<>();
    //     for (MultipartFile multipartFile : multipartFiles) {
    //         savedFileDtoList.add(getAttachFile(multipartFile, fileType));
    //     }
    //     List<String> urlList = savedFileDtoList
    //             .stream()
    //             .map(savedFileDto -> savedFileDto.getFileDirectory() + savedFileDto.getFileName())
    //             .toList();
    //
    //
    //     chatMessageDto.setUrlList(urlList);
    //     return savedFileDtoList;
    // }

    @Transactional
    public List<SavedFileDto> saveAttachFile(MultipartFile[] multipartFiles, ChatMessageDto chatMessageDto) throws IOException {

        ChatMessage chatMessage = chatMessageService.saveMessage(chatMessageDto);
        List<SavedFileDto> savedFileDtoList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            savedFileDtoList.add(getAttachFile(multipartFile, chatMessage));
        }

        List<SavedFileDto> fileInfoDtoList = savedFileDtoList
                .stream()
                .map(savedFileDto -> {
                    SavedFileDto fileInfoDto = new SavedFileDto();
                    fileInfoDto.setFileName(savedFileDto.getFileName());
                    fileInfoDto.setFileType(savedFileDto.getFileType());
                    fileInfoDto.setFileDirectory(savedFileDto.getFileDirectory());
                    fileInfoDto.setFileSize(savedFileDto.getFileSize());
                    return fileInfoDto;
                })
                .toList();

        chatMessageDto.setFileList(fileInfoDtoList);
        subscriber.sendToUser(objectMapper.writeValueAsString(chatMessageDto));
        return savedFileDtoList;
    }


    private SavedFileDto getAttachFile(MultipartFile multipartFile, ChatMessage chatMessage) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        Optional<String> optionalExt = Optional.of(originalFileName.substring(originalFileName.lastIndexOf(".") + 1));
        String savedFilename = getSavedFileImage(optionalExt);
        String savedFileType = getFileType(multipartFile.getContentType());
        String savedFileDirectory = getSavedFileDirectory(savedFileType);
        String savedFileFullPath = getSavedFileFullPath(savedFileDirectory, savedFilename);
        String savedFileSize = Double.toString(getFileSize(multipartFile)) + " MB";

        log.info("file type =======> {}", multipartFile.getContentType());

        isDirectoryExist(savedFileDirectory);

        multipartFile.transferTo(Path.of(savedFileFullPath));
        log.info("파일을 저장했습니다. 저장위치: {}", savedFileFullPath);

        AttachFile attachFile = AttachFile.builder()
                .chatMessage(chatMessage)
                .originalFileName(originalFileName)
                .savedFileName(savedFilename)
                .fileType(savedFileType)
                .fileDirectory(savedFileDirectory)
                .ext(optionalExt.orElse(null))
                .fileSize(savedFileSize)
                .build();

        AttachFile savedAttachFile = attachFileRepository.save(attachFile);
        return new SavedFileDto(savedAttachFile);
    }

    private static String getSavedFileFullPath(String savedFileDirectory, String savedFileType) {
        if (savedFileDirectory.endsWith("/")) {
            return savedFileDirectory + savedFileType;
        }

        return savedFileDirectory + "/" + savedFileType;
    }

    private String getSavedFileDirectory(String savedFileType) {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String cntDate = localDateTime.format(dateTimeFormatter);

        return new StringBuffer(savedFilePath)
                .append("/")
                .append(savedFileType)
                .append("/")
                .append(cntDate)
                .toString();
    }

    public Resource getFileResource(String fileName) throws MalformedURLException, FileNotFoundException {
        Optional<AttachFile> bySavedFileName = attachFileRepository.findBySavedFileName(fileName);
        if (bySavedFileName.isEmpty()) {
            throw new FileNotFoundException("해당 파일은 존재하지 않습니다.");
        }
        String savedFileFullPath = getSavedFileFullPath(bySavedFileName.get().getFileDirectory(), bySavedFileName.get().getSavedFileName());

        FileUrlResource fileUrlResource = new FileUrlResource(savedFileFullPath);
        return fileUrlResource;
    }

    // public FileResponse getFileResource(String fileName) throws MalformedURLException, FileNotFoundException {
    //     Optional<AttachFile> bySavedFileName = attachFileRepository.findBySavedFileName(fileName);
    //     if (bySavedFileName.isEmpty()) {
    //         throw new FileNotFoundException("해당 파일은 존재하지 않습니다.");
    //     }
    //     String savedFileFullPath = getSavedFileFullPath(bySavedFileName.get().getFileDirectory(), bySavedFileName.get().getSavedFileName());
    //
    //     FileUrlResource fileUrlResource = new FileUrlResource(savedFileFullPath);
    //
    //     FileResponse fileResponse = new FileResponse();
    //     fileResponse.setResource(fileUrlResource);
    //     fileResponse.setFileName(bySavedFileName.get().getSavedFileName());
    //     fileResponse.setType(bySavedFileName.get().getFileType());
    //
    //     return fileResponse;
    // }

    private String getSavedFileImage(Optional<String> optionalExt) {
        String fileName = UUID.randomUUID().toString();

        if (optionalExt.isPresent()) {
            fileName += "." + optionalExt.get();
        }

        return fileName;
    }

    private String getFileType(String fileType) {
        if (fileType == null) {
            return AttachFileType.FILE.getValue();
        }

        if (fileType.contains(AttachFileType.IMAGE.getValue())) {
            return AttachFileType.IMAGE.getValue();
        } else {
            return AttachFileType.FILE.getValue();
        }
    }

    public static double getFileSize(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 존재하지 않거나 비어 있습니다.");
        }
        return Math.round(file.getSize() / (1024.0 * 1024.0) * 100.0) / 100.0; // 파일 크기를 바이트 단위로 반환
    }

    // private String getFileType(String fileType) {
    //     if (fileType == null) {
    //         return AttachFileType.FILE.getValue();
    //     }
    //
    //     // if (fileType.equals(AttachFileType.PROFILE.getValue())) {
    //     //     return AttachFileType.PROFILE.getValue();
    //     // } else if (fileType.equals(AttachFileType.IMAGE.getValue())) {
    //     //     return AttachFileType.IMAGE.getValue();
    //     // } else {
    //     //     return AttachFileType.FILE.getValue();
    //     // }
    //
    //     if (fileType.equals(AttachFileType.PROFILE.getValue())) {
    //         return AttachFileType.PROFILE.getValue();
    //     } else if (fileType.equals(AttachFileType.IMAGE.getValue())) {
    //         return AttachFileType.IMAGE.getValue();
    //     } else {
    //         return AttachFileType.FILE.getValue();
    //     }
    // }

    private void isDirectoryExist(String filePath) {
        File file = new File(filePath);
        file.mkdirs();
    }

}
