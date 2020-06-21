package com.greenlearner.fileUploadDownload.controller;

import com.greenlearner.fileUploadDownload.dto.FileUploadResponse;
import com.greenlearner.fileUploadDownload.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */

@RestController
public class UploadDownloadWithFileSystemController {

    private FileStorageService fileStorageService;

    public UploadDownloadWithFileSystemController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("single/upload")
    FileUploadResponse singleFileUplaod(@RequestParam("file") MultipartFile file) {

        String fileName = fileStorageService.storeFile(file);

        ///http://localhost:8081/download/abc.jpg
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(fileName)
                .toUriString();

        String contentType = file.getContentType();

        FileUploadResponse response = new FileUploadResponse(fileName, contentType, url);

        return response;

    }

    @GetMapping("/download/{fileName}")
    ResponseEntity<Resource>downLoadSingleFile(@PathVariable String fileName, HttpServletRequest request){

        Resource resource = fileStorageService.downloadFile(fileName);

//        MediaType contentType = MediaType.APPLICATION_PDF;

        String mimeType;

        try {
            mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName="+resource.getFilename())
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName="+resource.getFilename())
                .body(resource);
    }

    @PostMapping("/multiple/upload")
    List<FileUploadResponse> multipleUpload(@RequestParam("files") MultipartFile[] files){

        if(files.length > 7){
            throw new RuntimeException("too many files");
        }
        List<FileUploadResponse> uploadResponseList = new ArrayList<>();
        Arrays.asList(files)
                .stream()
                .forEach(file -> {
                    String fileName = fileStorageService.storeFile(file);

                    ///http://localhost:8081/download/abc.jpg
                    String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/download/")
                            .path(fileName)
                            .toUriString();

                    String contentType = file.getContentType();

                    FileUploadResponse response = new FileUploadResponse(fileName, contentType, url);
                    uploadResponseList.add(response);
                });

        return uploadResponseList;
    }

    @GetMapping("zipDownload")
    void zipDownload(@RequestParam("fileName") String[] files, HttpServletResponse response) throws IOException {
//zipoutstream - zipentry+zipentry

        try(ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())){
            Arrays.asList(files)
                    .stream()
                    .forEach(file -> {
                        Resource resource = fileStorageService.downloadFile(file);

                        ZipEntry zipEntry = new ZipEntry(resource.getFilename());

                        try {
                            zipEntry.setSize(resource.contentLength());
                            zos.putNextEntry(zipEntry);

                            StreamUtils.copy(resource.getInputStream(), zos);

                            zos.closeEntry();
                        } catch (IOException e) {
                            System.out.println("some exception while ziping");
                        }
                    });
            zos.finish();

        }

        response.setStatus(200);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=zipfile");
    }
}
