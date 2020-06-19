package com.greenlearner.fileoperations;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */
@Controller
public class FeCOntroller {

    @GetMapping()
    ModelAndView modelAndView(){
        return new ModelAndView("formUpload");
    }
}
