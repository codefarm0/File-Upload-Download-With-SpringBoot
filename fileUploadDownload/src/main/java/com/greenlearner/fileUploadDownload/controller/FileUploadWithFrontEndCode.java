package com.greenlearner.fileUploadDownload.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */

@Controller
public class FileUploadWithFrontEndCode {

    @GetMapping("/files")
    ModelAndView fileUpload(){
        Model m = new ExtendedModelMap();
        return new ModelAndView("index.html");
    }
}
