package ru.cashbackManager.service.cashback;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.cashbackManager.dto.cashback.AddCashbackRequest;
import ru.cashbackManager.dto.cashback.CardWithCashbacksResponse;
import ru.cashbackManager.dto.cashback.DeleteCashbackRequest;
import ru.cashbackManager.model.Card;
import ru.cashbackManager.model.Cashback;
import ru.cashbackManager.model.Category;
import ru.cashbackManager.repository.CashbackRepository;
import ru.cashbackManager.repository.CategoryRepository;
import ru.cashbackManager.service.card.CardService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CashbackManagerServiceTest {

    private static final LocalDateTime MAX_DATE = LocalDateTime.of(9999, 12, 31, 23, 59, 59);
    private static final String TEST_CARD_NAME = "Test Card";
    private static final String TEST_CATEGORY_NAME = "Test Category";
    private static final double TEST_PERCENT = 10.0;

    @Mock
    private CashbackRepository cashbackRepository;

    @Mock
    private CardService cardService;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CashbackManagerService cashbackManagerService;

    @Test
    void addCurrentCashback_Permanent() {
        AddCashbackRequest request = new AddCashbackRequest();
        request.setCardName(TEST_CARD_NAME);
        request.setCategoryName(TEST_CATEGORY_NAME);
        request.setPercent(TEST_PERCENT);
        request.setPermanent(true);

        Card card = new Card();
        card.setName(TEST_CARD_NAME);

        Category category = new Category();
        category.setName(TEST_CATEGORY_NAME);

        when(cardService.findCardByName(anyString())).thenReturn(card);
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));
        when(cashbackRepository.saveAndFlush(any(Cashback.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cashback result = cashbackManagerService.addCurrentCashback(request);

        assertNotNull(result);
        assertEquals(TEST_PERCENT / 100.0, result.getPercent());
        assertEquals(MAX_DATE, result.getEndDate());

        verify(cashbackRepository, times(1)).saveAndFlush(any(Cashback.class));
    }

    @Test
    void addCurrentCashback_NonPermanent() {
        AddCashbackRequest request = new AddCashbackRequest();
        request.setCardName(TEST_CARD_NAME);
        request.setCategoryName(TEST_CATEGORY_NAME);
        request.setPercent(TEST_PERCENT);
        request.setPermanent(false);

        Card card = new Card();
        card.setName(TEST_CARD_NAME);

        Category category = new Category();
        category.setName(TEST_CATEGORY_NAME);

        when(cardService.findCardByName(anyString())).thenReturn(card);
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));
        when(cashbackRepository.saveAndFlush(any(Cashback.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cashback result = cashbackManagerService.addCurrentCashback(request);

        assertNotNull(result);
        assertEquals(TEST_PERCENT / 100.0, result.getPercent());

        verify(cashbackRepository, times(1)).saveAndFlush(any(Cashback.class));
    }

    @Test
    void deleteCurrentCashback() {
        DeleteCashbackRequest request = new DeleteCashbackRequest();
        request.setCardName(TEST_CARD_NAME);
        request.setCategoryName(TEST_CATEGORY_NAME);

        Card card = new Card();
        card.setId(1L);
        card.setName(TEST_CARD_NAME);

        Category category = new Category();
        category.setId(1L);
        category.setName(TEST_CATEGORY_NAME);

        when(cardService.findCardByName(anyString())).thenReturn(card);
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));

        cashbackManagerService.deleteCurrentCashback(request);

        verify(cashbackRepository, times(1)).deleteByCardIdAndCategoryIdAndEndDate(
                eq(card.getId()), eq(category.getId()), any(LocalDateTime.class));
    }

    @Test
    void getAllCashbacks() {
        Card card = new Card();
        card.setName(TEST_CARD_NAME);

        Category category = new Category();
        category.setName(TEST_CATEGORY_NAME);

        Cashback cashback = new Cashback();
        cashback.setCard(card);
        cashback.setCategory(category);
        cashback.setPercent(TEST_PERCENT / 100.0);

        when(cashbackRepository.findAll()).thenReturn(List.of(cashback));

        List<CardWithCashbacksResponse> responses = cashbackManagerService.getAllCashbacks();

        assertNotNull(responses);
        assertFalse(responses.isEmpty());

        CardWithCashbacksResponse response = responses.get(0);
        assertEquals(TEST_CARD_NAME, response.getName());
        assertEquals(TEST_PERCENT, response.getCashbacks().get(TEST_CATEGORY_NAME));

        verify(cashbackRepository, times(1)).findAll();
    }
}
