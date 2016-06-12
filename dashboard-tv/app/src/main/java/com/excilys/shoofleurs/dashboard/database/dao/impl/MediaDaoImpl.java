package com.excilys.shoofleurs.dashboard.database.dao.impl;

import com.excilys.shoofleurs.dashboard.database.dao.MediaDao;
import com.excilys.shoofleurs.dashboard.model.entities.Media;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Book;
import io.paperdb.Paper;

/**
 * @author Tommy Buonomo on 10/06/16.
 */
public class MediaDaoImpl implements MediaDao {
    private Book mediaBook = Paper.book(MediaDao.ENTITY_NAME);

    @Override
    public Media get(String id) {
        return mediaBook.read(id);
    }

    @Override
    public List<Media> getAll() {
        List<Media> medias = new ArrayList<>();
        for (String key : mediaBook.getAllKeys()) {
            medias.add((Media) mediaBook.read(key));
        }
        return medias;
    }

    @Override
    public void save(Media media) {
        if (media == null) {
            return;
        }
        mediaBook.write(media.getUuid(), media);
    }

    @Override
    public void saveAll(List<Media> medias) {
        if (medias != null) {
            for (Media media : medias) {
                save(media);
            }
        }
    }

    @Override
    public void delete(String id) {
        mediaBook.delete(id);
    }
}
