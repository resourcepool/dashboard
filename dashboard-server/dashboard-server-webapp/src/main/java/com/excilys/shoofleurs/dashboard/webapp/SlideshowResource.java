package com.excilys.shoofleurs.dashboard.webapp;

import com.excilys.shoofleurs.dashboard.business.SlideshowService;
import com.excilys.shoofleurs.dashboard.entities.Slideshow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("slideshows")
public class SlideshowResource {

    @Autowired
    private SlideshowService slideshowService;


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Slideshow> create(@RequestBody Slideshow slideshow) {
        slideshow = slideshowService.save(slideshow);
        if (slideshow == null || slideshow.getId() == 0) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(slideshow, HttpStatus.CREATED);
    }
}
