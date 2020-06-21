package com.greenlearner.fileUploadDownload.dto;

import javax.persistence.*;

/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */
@Entity
public class FileDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "filename")
    private String fileName;

    @Column(name = "docfile")
    @Lob
    private byte[] docFile;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getDocFile() {
        return docFile;
    }

    public void setDocFile(byte[] docFile) {
        this.docFile = docFile;
    }
}
