package com.excilys.shoofleurs.dashboard.business;

import com.excilys.shoofleurs.dashboard.entities.flash.Flash;
import com.excilys.shoofleurs.dashboard.persistence.FlashDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlashService {

    @Autowired
    private FlashDao flashDao;

    public Flash save(Flash flash) {
        return flashDao.save(flash);
    }

    public Flash findById(int id) {
        return flashDao.findOne(id);
    }

    public List<Flash> findAll() {
        return flashDao.findAll();
    }

    public void delete(int id) {
        flashDao.delete(id);
    }
}
