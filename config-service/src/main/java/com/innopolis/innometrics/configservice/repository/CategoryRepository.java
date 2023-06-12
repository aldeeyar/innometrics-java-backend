package com.innopolis.innometrics.configservice.repository;

import com.innopolis.innometrics.configservice.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    Boolean existsByCatName(String catName);
}
