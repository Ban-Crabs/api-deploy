package com.bancrabs.villaticket.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bancrabs.villaticket.models.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, String>{
    List<Category> findByName(String name);
}
