package com.excilys.shoofleurs.dashboard.persistence;

import com.excilys.shoofleurs.dashboard.entities.flash.Flash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashDao extends JpaRepository<Flash, Integer> {
}
