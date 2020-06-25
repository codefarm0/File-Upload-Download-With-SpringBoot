package com.greenlearner.fileUploadDownload.controller;

import com.greenlearner.fileUploadDownload.service.FileStorageService;
import org.assertj.core.internal.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */

@SpringBootTest
@AutoConfigureMockMvc
public class UploadDownloadWithFileSystemControllerTest {

    @Autowired
   private MockMvc mockMvc;

    @MockBean
    private FileStorageService fileStorageService;

    @Test
    @DisplayName("test to upload single file")
    void shouldUploadSingleFile() throws Exception {

        Mockito.when(fileStorageService.storeFile(any(MultipartFile.class))).thenReturn("test-file.txt");

        MockMultipartFile mmf = new MockMultipartFile("file", "test-file.txt",
                "text/plain" , "Green Learner - Arvind".getBytes());

        this.mockMvc.perform(multipart("/single/upload").file(mmf))
                .andExpect(status().isOk())
        .andExpect(content().json("{\"fileName\":test-file.txt,\"contentType\":\"text/plain\",\"url\":\"http://localhost/download/test-file.txt\"}"));

        then(this.fileStorageService).should().storeFile(mmf);

    }

    @Test
    @DisplayName("single file download test")
    void singleFileDownload() throws Exception{
        Resource resource = getMockedResource();
        Mockito.when(fileStorageService.downloadFile(anyString())).thenReturn(resource);

        mockMvc.perform(get("/download/abc.jpg"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName="+resource.getFilename()))
                .andExpect(content().bytes("Green Learner".getBytes()));

        BDDMockito.then(fileStorageService).should().downloadFile("abc.jpg");
    }

    private Resource getMockedResource() {
        Resource resource = new Resource() {
            @Override
            public boolean exists() {
                return false;
            }

            @Override
            public URL getURL() throws IOException {
                return null;
            }

            @Override
            public URI getURI() throws IOException {
                return null;
            }

            @Override
            public File getFile() throws IOException {
                File file = new File("dummy.jpeg");
                System.out.println(file.getAbsolutePath());
                return file;
            }

            @Override
            public long contentLength() throws IOException {
                return 0;
            }

            @Override
            public long lastModified() throws IOException {
                return 0;
            }

            @Override
            public Resource createRelative(String relativePath) throws IOException {
                return null;
            }

            @Override
            public String getFilename() {
                return "dummy.jpeg";
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream("Green Learner".getBytes());
            }
        };
        return resource;
    }
}
