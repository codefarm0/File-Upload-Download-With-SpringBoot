package com.greenlearner.fileUploadDownload.service;

import com.greenlearner.fileUploadDownload.dto.FileDocument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */

@Repository
public interface DocFileDao extends CrudRepository<FileDocument, Long> {

    FileDocument findByFileName(String fileName);
}
