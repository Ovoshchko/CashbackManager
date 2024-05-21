package ru.cashbackManager.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.cashbackManager.dto.card.GetBestCashbackCardRequest;
import ru.cashbackManager.exception.NoCardForCashbackException;
import ru.cashbackManager.model.Cashback;
import ru.cashbackManager.model.Category;
import ru.cashbackManager.repository.CashbackRepository;
import ru.cashbackManager.repository.CategoryRepository;
import ru.cashbackManager.service.transaction.TransactionService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.cashbackManager.repository.CategoryRepository.OTHER_CATEGORY;

@Service
@RequiredArgsConstructor
public class CategoryCashbackService implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionService transactionService;
    private final CashbackRepository cashbackRepository;

    @Override
    public Category addCategory(Category category) {
        try {
            return categoryRepository.saveAndFlush(
                    new Category().setName(category.getName())
            );
        } catch (DataIntegrityViolationException ignore) {
            return categoryRepository.findByName(category.getName()).orElse(null);
        }
    }

    @Override
    public Category findCategoryByName(String name) {
        return categoryRepository.findByName(name).orElse(null);
    }

    @Override
    public String getBestCard(GetBestCashbackCardRequest request) {
        List<Category> categories = categoryRepository.findByNameIn(
                List.of(request.getCategoryName(), OTHER_CATEGORY)
        );

        long price = (request.getAmount() == null) ? (long)1000000000 : request.getAmount().longValue();

        if (!categories.isEmpty()) {
            Map<String, Long> transactions =
                    transactionService.getTransactionsForBankCurrentMonth();

            List<Cashback> allCashbacks = new ArrayList<>();

            for (Category category: categories) {
                allCashbacks.addAll(
                        cashbackRepository.findByCategoryIdAndCurrentDate(category.getId(), LocalDateTime.now())
                );
            }

            Optional<Map.Entry<String, Cashback>> max = allCashbacks.stream()
                    .collect(Collectors.groupingBy(
                            cashback -> cashback.getCard().getName(),
                            Collectors.reducing((a, b) -> a.getPercent() >= b.getPercent() ? a : b)
                    ))
                    .values().stream()
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toMap(
                            cashback -> cashback.getCard().getName(),
                            cashback -> cashback
                    ))
                    .entrySet().stream()
                    .max(Comparator.comparingDouble(entry ->
                            Math.min(entry.getValue().getPercent() * price,
                                    transactions.getOrDefault(entry.getValue().getCard().getBank().getName(), 0L))
                    ));

            if (max.isPresent()) {
                return max.get().getKey();
            }
        }

        throw new NoCardForCashbackException();
    }
}
