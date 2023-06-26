package com.bancrabs.villaticket.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bancrabs.villaticket.models.entities.Locale;

public interface LocaleRepository extends JpaRepository<Locale, String>{
    
}
