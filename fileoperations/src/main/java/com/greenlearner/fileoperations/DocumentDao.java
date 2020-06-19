package com.greenlearner.fileoperations;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */
@Repository
public interface DocumentDao extends CrudRepository<Document, Long> {
    Document findByDocName(String fileName);
}
