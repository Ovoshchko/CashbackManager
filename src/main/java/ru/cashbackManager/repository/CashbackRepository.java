package ru.cashbackManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.cashbackManager.model.Cashback;
import ru.cashbackManager.model.Category;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CashbackRepository extends JpaRepository<Cashback, Long> {

    void deleteByCardIdAndCategoryIdAndEndDate(long cardId, long categoryId, LocalDateTime endDate);

    Cashback findByCard_NameAndCategory_Name(String cardName, String categoryName);

    @Query("SELECT c FROM Cashback c WHERE c.category.id = :categoryId AND c.startDate <= :currentDate AND c.endDate >= :currentDate")
    List<Cashback> findByCategoryIdAndCurrentDate(@Param("categoryId") Long categoryId, @Param("currentDate") LocalDateTime currentDate);
}
