package ru.cashbackManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.cashbackManager.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    String OTHER_CATEGORY = "Остальное";

    Optional<Category> findByName(String name);

    @Query("SELECT c FROM Category c WHERE c.name IN :names")
    List<Category> findByNameIn(List<String> names);
}
