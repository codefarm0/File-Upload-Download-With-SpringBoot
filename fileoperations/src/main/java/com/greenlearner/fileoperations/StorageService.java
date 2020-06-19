package com.greenlearner.fileoperations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */
@Service
public class StorageService {


    private  String fileStorageLocation;

    private final Path pathLocation;

    public StorageService( @Value("${file.storage.location:null}")String fileStorageLocation) {
        if (fileStorageLocation == null){
            throw new RuntimeException("File storage path is mandatory !! Exiting!!");
        }

        pathLocation=Paths.get(fileStorageLocation).toAbsolutePath().normalize();

        try {
            Files.createDirectories(pathLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create the storage directory");
        }
    }

    public Resource loadAsResource(String fileName) {
        try {

            Path file = Paths.get("storage").toAbsolutePath().resolve(fileName);

            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new RuntimeException(
                        "Could not read file: " + fileName);

            }
        }
        catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + fileName, e);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path path = Paths.get(pathLocation + "\\"+fileName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }
}
