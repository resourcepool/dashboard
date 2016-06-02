package com.excilys.shoofleurs.dashboard.business;

import com.excilys.shoofleurs.dashboard.entities.Slideshow;
import com.excilys.shoofleurs.dashboard.persistence.SlideshowDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class SlideshowService {

    @Autowired
    private SlideshowDao slideshowDao;

    public Slideshow save(Slideshow slideshow) {
        return slideshowDao.save(slideshow);
    }

    public Page<Slideshow> findAll(int page, int size) {
        return slideshowDao.findAll(new PageRequest(page, size));
    }

    public Slideshow findById(int id) {
        return slideshowDao.findOne(id);
    }

    public void delete(int id) {
        slideshowDao.delete(id);
    }
}
