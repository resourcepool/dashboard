package com.excilys.shoofleurs.dashboard.persistence;

import com.excilys.shoofleurs.dashboard.entities.Slideshow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlideshowDao extends PagingAndSortingRepository<Slideshow, Integer> {

    @Query(name = "slideshow.find", value = "select s from slideshow s")
    Page<Slideshow> findAll(Pageable pageable);
}
