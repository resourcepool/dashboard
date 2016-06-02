package com.excilys.shoofleurs.dashboard.persistence;

import com.excilys.shoofleurs.dashboard.entities.content.AbstractContent;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentDao extends PagingAndSortingRepository<AbstractContent, Integer> {
}
