package ru.cashbackManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.cashbackManager.model.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t " +
            "WHERE EXTRACT(MONTH FROM t.transactionDate) = :month " +
            "AND EXTRACT(YEAR FROM t.transactionDate) = :year")
    List<Transaction> findTransactionsByMonth(@Param("month") int month, @Param("year") int year);
}
