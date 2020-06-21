package com.greenlearner.fileUploadDownload.controller;

import com.greenlearner.fileUploadDownload.dto.FileDocument;
import com.greenlearner.fileUploadDownload.dto.FileUploadResponse;
import com.greenlearner.fileUploadDownload.service.DocFileDao;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */

@RestController
public class UploadDownloadWithDatabaseController {

    private DocFileDao docFileDao;

    public UploadDownloadWithDatabaseController(DocFileDao docFileDao) {
        this.docFileDao = docFileDao;
    }

    @PostMapping("single/uploadDb")
    FileUploadResponse singleFileUplaod(@RequestParam("file") MultipartFile file) throws IOException {

        String name = StringUtils.cleanPath(file.getOriginalFilename());
        FileDocument fileDocument = new FileDocument();
        fileDocument.setFileName(name);
        fileDocument.setDocFile(file.getBytes());

        docFileDao.save(fileDocument);

        ///http://localhost:8081/download/abc.jpg
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFromDB/")
                .path(name)
                .toUriString();

        String contentType = file.getContentType();

        FileUploadResponse response = new FileUploadResponse(name, contentType, url);

        return response;

    }

    @GetMapping("/downloadFromDB/{fileName}")
    ResponseEntity<byte[]> downLoadSingleFile(@PathVariable String fileName, HttpServletRequest request) {

        FileDocument doc = docFileDao.findByFileName(fileName);

        String mimeType = request.getServletContext().getMimeType(doc.getFileName());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName="+resource.getFilename())
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + doc.getFileName())
                .body(doc.getDocFile());
    }
}
