package ru.cashbackManager.service.category;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.cashbackManager.dto.card.GetBestCashbackCardRequest;
import ru.cashbackManager.exception.NoCardForCashbackException;
import ru.cashbackManager.model.Bank;
import ru.cashbackManager.model.Card;
import ru.cashbackManager.model.Cashback;
import ru.cashbackManager.model.Category;
import ru.cashbackManager.repository.CashbackRepository;
import ru.cashbackManager.repository.CategoryRepository;
import ru.cashbackManager.service.transaction.TransactionService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryCashbackServiceTest {

    private static final String TEST_CATEGORY_NAME = "Test Category";
    private static final String TEST_CARD_NAME = "Test Card";
    private static final String TEST_BANK_NAME = "Test Bank";

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TransactionService transactionService;

    @Mock
    private CashbackRepository cashbackRepository;

    @InjectMocks
    private CategoryCashbackService categoryCashbackService;

    @Test
    void addCategory_NewCategory() {
        Category category = new Category();
        category.setName(TEST_CATEGORY_NAME);

        when(categoryRepository.saveAndFlush(any(Category.class))).thenReturn(category);

        Category result = categoryCashbackService.addCategory(category);

        assertNotNull(result);
        assertEquals(TEST_CATEGORY_NAME, result.getName());
        verify(categoryRepository, times(1)).saveAndFlush(any(Category.class));
    }

    @Test
    void addCategory_ExistingCategory() {
        Category category = new Category();
        category.setName(TEST_CATEGORY_NAME);

        when(categoryRepository.saveAndFlush(any(Category.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate"));
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));

        Category result = categoryCashbackService.addCategory(category);

        assertNotNull(result);
        assertEquals(TEST_CATEGORY_NAME, result.getName());
        verify(categoryRepository, times(1)).saveAndFlush(any(Category.class));
        verify(categoryRepository, times(1)).findByName(anyString());
    }

    @Test
    void findCategoryByName_Found() {
        Category category = new Category();
        category.setName(TEST_CATEGORY_NAME);

        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));

        Category result = categoryCashbackService.findCategoryByName(TEST_CATEGORY_NAME);

        assertNotNull(result);
        assertEquals(TEST_CATEGORY_NAME, result.getName());
        verify(categoryRepository, times(1)).findByName(anyString());
    }

    @Test
    void findCategoryByName_NotFound() {
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());

        Category result = categoryCashbackService.findCategoryByName(TEST_CATEGORY_NAME);

        assertNull(result);
        verify(categoryRepository, times(1)).findByName(anyString());
    }

    @Test
    void getBestCard_Found() {
        GetBestCashbackCardRequest request = new GetBestCashbackCardRequest();
        request.setCategoryName(TEST_CATEGORY_NAME);
        request.setAmount(2000);

        Category category = new Category();
        category.setId(1L);
        category.setName(TEST_CATEGORY_NAME);

        Cashback cashback = new Cashback();
        cashback.setPercent(0.1);
        cashback.setCard(new Card().setName(TEST_CARD_NAME).setBank(new Bank().setName(TEST_BANK_NAME)));

        when(categoryRepository.findByNameIn(anyList())).thenReturn(List.of(category));
        when(transactionService.getTransactionsForBankCurrentMonth()).thenReturn(Map.of(TEST_BANK_NAME, 500L));
        when(cashbackRepository.findByCategoryIdAndCurrentDate(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(cashback));

        String result = categoryCashbackService.getBestCard(request);

        assertNotNull(result);
        assertEquals(TEST_CARD_NAME, result);
        verify(categoryRepository, times(1)).findByNameIn(anyList());
        verify(transactionService, times(1)).getTransactionsForBankCurrentMonth();
        verify(cashbackRepository, times(1))
                .findByCategoryIdAndCurrentDate(anyLong(), any(LocalDateTime.class));
    }

    @Test
    void getBestCard_NotFound() {
        GetBestCashbackCardRequest request = new GetBestCashbackCardRequest();
        request.setCategoryName(TEST_CATEGORY_NAME);
        request.setAmount(2000);

        when(categoryRepository.findByNameIn(anyList())).thenReturn(Collections.emptyList());

        assertThrows(NoCardForCashbackException.class, () -> categoryCashbackService.getBestCard(request));

        verify(categoryRepository, times(1)).findByNameIn(anyList());
        verify(transactionService, times(0)).getTransactionsForBankCurrentMonth();
        verify(cashbackRepository, times(0)).findByCategoryIdAndCurrentDate(anyLong(), any(LocalDateTime.class));
    }
}
