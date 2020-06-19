package com.greenlearner.fileoperations;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */

@RestController
public class FileController {

    private StorageService storageService;
    private DocumentDao dao;

    public FileController(StorageService storageService,
                          DocumentDao dao) {
        this.storageService = storageService;
        this.dao = dao;

    }

    @GetMapping("/download/{fileName}")
    ResponseEntity<Resource> hello(@PathVariable String fileName) {

        System.out.println("filename - " + fileName);
        Resource resource = storageService.loadAsResource(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=" + resource.getFilename())
                .body(resource);
    }

    @PostMapping("upload/")
    UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {

        String fileName = storageService.storeFile(file);

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, uri, file.getContentType(), file.getSize());
    }

    @PostMapping("multiUpload/")
    List<UploadFileResponse> uploadFiles(@RequestParam("file") MultipartFile[] file) {

        List<MultipartFile> fileList = Arrays.asList(file);

        List<String> fileName = new ArrayList();
        fileList.stream()
                .forEach(
                        e -> {
                            String s = storageService.storeFile(e);
                            fileName.add(s);
                        }
                );
//        String fileName = storageService.storeFile(file);
        List<UploadFileResponse> responses = new ArrayList<>();
        fileName.stream().forEach(
                name -> {
                    String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/download/")
                            .path(name)
                            .toUriString();
                    responses.add(new UploadFileResponse(name, uri, null, 0));
                }
        );
        return responses;
    }

    @PostMapping("uploadDb/")
    List<UploadFileResponse> uploadFilesDB(@RequestParam("file") MultipartFile[] file) {

        List<MultipartFile> fileList = Arrays.asList(file);


        List<String> fileName = new ArrayList();
        fileList.stream()
                .forEach(
                        e -> {
                            String name = StringUtils.cleanPath(e.getOriginalFilename());
                            Document doc = new Document();
                            doc.setDocName(name);
                            try {
                                doc.setFile(e.getBytes());
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                            dao.save(doc);
                            fileName.add(name);
                        }
                );
//        String fileName = storageService.storeFile(file);
        List<UploadFileResponse> responses = new ArrayList<>();
        fileName.stream().forEach(
                name -> {
                    String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/download/")
                            .path(name)
                            .toUriString();
                    responses.add(new UploadFileResponse(name, uri, null, 0));
                }
        );
        return responses;
    }

    @GetMapping("/downloadDb/{fileName}")
    ResponseEntity<byte[]> downloadDB(@PathVariable String fileName) {

        System.out.println("filename - " + fileName);
        //Resource resource = storageService.loadAsResource(fileName);

        Document doc = dao.findByDocName(fileName);
        //doc.getFile();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                //.contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=" + fileName)
                .body(doc.getFile());
    }


}
