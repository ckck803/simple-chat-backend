package org.example.simplechat.file.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.example.simplechat.chat.message.dto.ChatMessageDto;
import org.example.simplechat.chat.message.entity.ChatMessage;
import org.example.simplechat.chat.message.service.ChatMessageService;
import org.example.simplechat.chat.message.subscribe.ChatMessageSubscriber;
import org.example.simplechat.file.constant.AttachFileType;
import org.example.simplechat.file.dto.FileResourceDto;
import org.example.simplechat.file.dto.SavedFileDto;
import org.example.simplechat.file.entity.AttachFile;
import org.example.simplechat.file.repository.AttachFileRepository;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
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
                    fileInfoDto.setThumbnail(savedFileDto.getThumbnail());
                    return fileInfoDto;
                })
                .toList();

        chatMessageDto.setFileList(fileInfoDtoList);
        subscriber.sendToUser(objectMapper.writeValueAsString(chatMessageDto));
        return savedFileDtoList;
    }


    private SavedFileDto getAttachFile(MultipartFile multipartFile, ChatMessage chatMessage) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        Optional<String> optionalExt = getSavedFileExt(originalFileName);
        String savedFilename = getSavedFileImage(optionalExt);
        String savedFileType = getFileType(multipartFile.getContentType());
        String savedFileDirectory = getSavedFileDirectory(savedFileType);
        String savedFileFullPath = getSavedFileFullPath(savedFileDirectory, savedFilename);
        String savedFileSize = getSavedFileSize(multipartFile);

        Path path = saveFile(multipartFile, savedFileDirectory, savedFileFullPath);
        log.info("파일을 저장했습니다. 저장위치: {}", savedFileFullPath);
        // Thumbnail 생성
        AttachFile savedThumbnail = createAndSaveThumbnail(savedFileType, path, originalFileName);

        // 파일 데이터 DB 에 저장
        AttachFile attachFile = AttachFile.builder()
                .chatMessage(chatMessage)
                .originalFileName(originalFileName)
                .savedFileName(savedFilename)
                .fileType(savedFileType)
                .contentType(multipartFile.getContentType())
                .fileDirectory(savedFileDirectory)
                .ext(optionalExt.orElse(null))
                .fileSize(savedFileSize)
                .thumbnailInfo(savedThumbnail)
                .build();

        AttachFile savedAttachFile = attachFileRepository.save(attachFile);
        log.info("attachFile.getThumbnailInfo().getSavedFileName() ===> {}", attachFile.getThumbnailInfo().getSavedFileName());
        return new SavedFileDto(savedAttachFile);
    }

    private AttachFile createAndSaveThumbnail(String savedFileType, Path path, String originalFileName) {
        AttachFile savedThumbnail = null;
        String thumbNailName = "";
        String savedThumbnailFileDirectory = getSavedFileDirectory(AttachFileType.THUMBNAIL.getValue());

        if (savedFileType.equals(AttachFileType.IMAGE.getValue())) {
            thumbNailName = createImageThumbnail(path.toFile(), savedThumbnailFileDirectory);
        } else if (savedFileType.equals(AttachFileType.VIDEO.getValue())) {
            thumbNailName = createVideoThumbnail(path.toFile(), savedThumbnailFileDirectory);
        }

        if (!thumbNailName.isEmpty()) {
            AttachFile thumbnailFile = AttachFile.builder()
                    .chatMessage(null)
                    .originalFileName(originalFileName)
                    .savedFileName(thumbNailName)
                    .fileType(AttachFileType.THUMBNAIL.getValue())
                    .fileDirectory(savedThumbnailFileDirectory)
                    .ext(getSavedFileExt(thumbNailName).orElse(null))
                    .fileSize(null)
                    .build();
            savedThumbnail = attachFileRepository.save(thumbnailFile);
        }
        return savedThumbnail;
    }

    private static Optional<String> getSavedFileExt(String originalFileName) {
        return Optional.of(originalFileName.substring(originalFileName.lastIndexOf(".") + 1));
    }

    private Path saveFile(MultipartFile multipartFile, String savedFileDirectory, String savedFileFullPath) throws IOException {
        isDirectoryExist(savedFileDirectory);
        Path path = Path.of(savedFileFullPath);
        multipartFile.transferTo(path);
        return path;
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

    public FileResourceDto getFileResource(String fileName) throws MalformedURLException, FileNotFoundException {
        Optional<AttachFile> bySavedFileName = attachFileRepository.findBySavedFileName(fileName);
        if (bySavedFileName.isEmpty()) {
            log.info("fileName =========> {}", fileName);
            throw new FileNotFoundException("해당 파일은 존재하지 않습니다.");
        }
        String savedFileFullPath = getSavedFileFullPath(bySavedFileName.get().getFileDirectory(), bySavedFileName.get().getSavedFileName());

        FileUrlResource fileUrlResource = new FileUrlResource(savedFileFullPath);
        FileResourceDto fileResourceDto = new FileResourceDto();
        fileResourceDto.setResource(fileUrlResource);
        fileResourceDto.setContentType(bySavedFileName.get().getContentType());

        return fileResourceDto;
    }

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
        } else if (fileType.contains(AttachFileType.VIDEO.getValue())) {
            return AttachFileType.VIDEO.getValue();
        } else {
            return AttachFileType.FILE.getValue();
        }
    }

    public String getSavedFileSize(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 존재하지 않거나 비어 있습니다.");
        }
        return Math.round(file.getSize() / (1024.0 * 1024.0) * 100.0) / 100.0 + " MB"; // 파일 크기를 바이트 단위로 반환
    }

    private void isDirectoryExist(String filePath) {
        File file = new File(filePath);
        file.mkdirs();
    }

    private String createImageThumbnail(File file, String fileDirectory) {
        try {
            isDirectoryExist(fileDirectory);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Thumbnails.of(new FileInputStream(file))
                    .size(200, 200)
                    .outputFormat("png")
                    .toOutputStream(baos);

            String thumbnailName = "thumbnail_" + System.currentTimeMillis() + ".png";

            File outputFile = new File(fileDirectory, thumbnailName);
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(baos.toByteArray());
            }
            return outputFile.getName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String createVideoThumbnail(File file, String fileDirectory) {
        SeekableByteChannel channel = null;
        try {
            isDirectoryExist(fileDirectory);

            channel = NIOUtils.readableChannel(file);
            FrameGrab grab = FrameGrab.createFrameGrab(channel);

            // (선택) 5초 지점으로 이동
            // seekToSecondPrecise(5.0)은 5초 정확히 찾고 프레임을 가져오려고 시도
            // 동영상이 5초보다 짧다면 예외(또는 null)가 발생할 수 있음
            grab.seekToSecondPrecise(5.0);

            Picture picture = grab.getNativeFrame();
            if (picture == null) {
                log.error("동영상 길이가 5초보다 짧거나, 프레임 추출에 실패했습니다.");
            }

            BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);

            // 5) 추출한 이미지를 원하는 경로나 형식으로 저장
            //    여기서는 예시로 임시 디렉토리에 JPG 파일로 저장
            String thumbnailName = "thumbnail_" + System.currentTimeMillis() + ".png";
            File outputFile = new File(fileDirectory, thumbnailName);
            ImageIO.write(bufferedImage, "png", outputFile);

            // ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Thumbnails.of(bufferedImage)
            //           .size(320, 180)
            //           .outputFormat("jpg")
            //           .toOutputStream(baos);
            // byte[] resizedThumbnail = baos.toByteArray();

            log.info("썸네일 생성 성공: " + outputFile.getAbsolutePath());
            return outputFile.getName();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("JCodec 썸네일 생성 중 오류가 발생했습니다.");
        } catch (JCodecException e) {
            throw new RuntimeException(e);
        } finally {
            // 6) 리소스 정리
            if (channel != null) {
                NIOUtils.closeQuietly(channel);
            }
        }
        return "";
    }
}
