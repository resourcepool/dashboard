package com.excilys.shoofleurs.dashboard.business;

import com.excilys.shoofleurs.dashboard.entities.content.AbstractContent;
import com.excilys.shoofleurs.dashboard.persistence.ContentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentService {

    @Autowired
    private ContentDao contentDao;

    public AbstractContent save(AbstractContent abstractContent) {
        return contentDao.save(abstractContent);
    }

    public void delete(int id) {
        contentDao.delete(id);
    }

    public AbstractContent findById(int id) {
        return contentDao.findOne(id);
    }
}
