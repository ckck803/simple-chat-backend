package org.example.simplechat.file.controller;

import lombok.RequiredArgsConstructor;
import org.example.simplechat.chat.message.dto.ChatMessageDto;
import org.example.simplechat.file.constant.AttachFileType;
import org.example.simplechat.file.dto.SavedFileDto;
import org.example.simplechat.file.service.AttachFileService;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AttachFileController {

    private final AttachFileService attachFileService;

    @GetMapping("/api/file/{filename}")
    public ResponseEntity<Resource> getFileResource(@PathVariable("filename") String filename) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.IMAGE_PNG);
            httpHeaders.setContentDisposition(ContentDisposition.inline().filename("image.png").build());

            Resource fileResource = attachFileService.getFileResource(filename);
            return new ResponseEntity<>(fileResource, httpHeaders, HttpStatus.OK);
        } catch (MalformedURLException exception) {
            return ResponseEntity.badRequest().body(null);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // @GetMapping("/api/file/{filename}")
    // public ResponseEntity<FileResponse> getFileResource(@PathVariable("filename") String filename) {
    //     try {
    //         FileResponse fileResponse = attachFileService.getFileResource(filename);
    //         HttpHeaders httpHeaders = new HttpHeaders();
    //         if(fileResponse.getType() == AttachFileType.IMAGE.getValue()){
    //             httpHeaders.setContentType(MediaType.IMAGE_PNG);
    //             httpHeaders.setContentDisposition(ContentDisposition.inline().filename(fileResponse.getFileName()).build());
    //         }else if(fileResponse.getType() == AttachFileType.FILE.getValue()){
    //             httpHeaders.setContentType(MediaType.APPLICATION_PDF);
    //             httpHeaders.setContentDisposition(ContentDisposition.inline().filename(fileResponse.getFileName()).build());
    //         }
    //
    //         return new ResponseEntity<>(fileResponse, httpHeaders, HttpStatus.OK);
    //     } catch (MalformedURLException exception) {
    //         return ResponseEntity.badRequest().body(null);
    //     } catch (FileNotFoundException e) {
    //         throw new RuntimeException(e);
    //     }
    // }

    // @PostMapping("/api/file")
    // public ResponseEntity<List<SavedFileDto>> saveFile(@RequestParam("file") MultipartFile[] multipartFiles, @RequestParam("fileType") String fileType) throws IOException {
    //     List<SavedFileDto> savedFileDtoList = attachFileService.saveAttachFile(multipartFiles, fileType);
    //     return ResponseEntity.created(null).body(savedFileDtoList);
    // }

    // @PostMapping("/api/file")
    // public ResponseEntity<List<SavedFileDto>> saveFile(@RequestPart("file") MultipartFile[] multipartFiles,
    //                                                    @RequestPart("fileType") String fileType,
    //                                                    @RequestPart("chatMessage") ChatMessageDto chatMessageDto) throws IOException {
    //     List<SavedFileDto> savedFileDtoList = attachFileService.saveAttachFile(multipartFiles, fileType, chatMessageDto);
    //     return ResponseEntity.created(null).body(savedFileDtoList);
    // }

    @PostMapping("/api/file")
    public ResponseEntity<List<SavedFileDto>> saveFile(@RequestPart("files") MultipartFile[] multipartFiles,
                                                       @RequestPart("chatMessage") ChatMessageDto chatMessageDto) throws IOException {
        List<SavedFileDto> savedFileDtoList = attachFileService.saveAttachFile(multipartFiles, chatMessageDto);
        return ResponseEntity.created(null).body(savedFileDtoList);
    }
}
