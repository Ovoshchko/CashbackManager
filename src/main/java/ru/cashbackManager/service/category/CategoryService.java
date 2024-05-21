package ru.cashbackManager.service.category;

import org.springframework.transaction.annotation.Transactional;
import ru.cashbackManager.dto.card.GetBestCashbackCardRequest;
import ru.cashbackManager.model.Category;

public interface CategoryService {

    @Transactional
    Category addCategory(Category category);

    Category findCategoryByName(String name);

    String getBestCard(GetBestCashbackCardRequest request);
}