package ru.cashbackManager.service.cashback;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CashbackManagerService implements CashbackService {

    private static final LocalDateTime MAX_DATE = LocalDateTime.of(9999, 12, 31, 23, 59, 59);
    private final CashbackRepository cashbackRepository;
    private final CardService cardService;
    private final CategoryRepository categoryRepository;

    @Override
    public Cashback addCurrentCashback(AddCashbackRequest request) {
        Cashback cashback = getCashbackEntityWithoutDate(request);

        if (request.isPermanent()) {
            return savePermanentCashBack(cashback, LocalDateTime.now());
        }

        return cashbackRepository.saveAndFlush(
                cashback
                        .setStartDate(LocalDateTime.now())
                        .setEndDate(
                                LocalDateTime.now()
                                        .with(TemporalAdjusters.firstDayOfNextMonth())
                                        .with(LocalTime.MIDNIGHT)
                        )
        );
    }

    @Override
    public Cashback addFutureCashback(AddCashbackRequest request) {
        Cashback cashback = getCashbackEntityWithoutDate(request);

        if (request.isPermanent()) {
            return savePermanentCashBack(cashback, LocalDateTime.now()
                    .with(TemporalAdjusters.firstDayOfNextMonth())
                    .with(LocalTime.MIDNIGHT));
        }

        return cashbackRepository.saveAndFlush(
                cashback
                        .setStartDate(
                                LocalDateTime.now()
                                        .with(TemporalAdjusters.firstDayOfNextMonth())
                                        .with(LocalTime.MIDNIGHT))
                        .setEndDate(
                                LocalDateTime.now()
                                        .with(TemporalAdjusters.firstDayOfNextMonth())
                                        .with(TemporalAdjusters.firstDayOfNextMonth())
                                        .with(LocalTime.MIDNIGHT)
                        )
        );
    }

    @Override
    public void deleteCurrentCashback(DeleteCashbackRequest deleteCashbackRequest) {
        Cashback cashback = getCashbackEntityForDelete(deleteCashbackRequest);
        cashbackRepository.deleteByCardIdAndCategoryIdAndEndDate(
                cashback.getCard().getId(),
                cashback.getCategory().getId(),
                LocalDateTime.now().with(TemporalAdjusters.firstDayOfNextMonth()).with(LocalTime.MIDNIGHT)
        );
    }

    @Override
    public void deleteFutureCashback(DeleteCashbackRequest deleteCashbackRequest) {
        Cashback cashback = getCashbackEntityForDelete(deleteCashbackRequest);
        cashbackRepository.deleteByCardIdAndCategoryIdAndEndDate(
                cashback.getCard().getId(),
                cashback.getCategory().getId(),
                LocalDateTime.now().with(TemporalAdjusters.firstDayOfNextMonth())
                        .with(TemporalAdjusters.firstDayOfNextMonth()).with(LocalTime.MIDNIGHT)
        );
    }

    @Override
    public List<CardWithCashbacksResponse> getAllCashbacks() {
        List<Cashback> cashbacks = cashbackRepository.findAll();
        return cashbacks.stream().collect(Collectors.groupingBy(Cashback::getCard))
                .entrySet().stream()
                .map(entry -> {
                    String cardName = entry.getKey().getName();
                    Map<String, Double> cardCashbacks = entry.getValue().stream()
                            .collect(Collectors.toMap(
                                    cashback -> cashback.getCategory().getName(),
                                    cashback -> Math.round(cashback.getPercent() * 100 * 100.0) / 100.0
                            ));
                    return new CardWithCashbacksResponse().setName(cardName).setCashbacks(cardCashbacks);
                })
                .toList();
    }

    @Override
    public Cashback getCashbackByCardNameAndCategory(String cardName, String categoryName) {
        return cashbackRepository.findByCard_NameAndCategory_Name(cardName, categoryName);
    }

    private Cashback savePermanentCashBack(Cashback cashback, LocalDateTime startDate) {
        return cashbackRepository.saveAndFlush(
                cashback.setStartDate(startDate).setEndDate(MAX_DATE)
        );
    }

    private Cashback getCashbackEntityWithoutDate(AddCashbackRequest request) {
        Card card = cardService.findCardByName(request.getCardName());

        Optional<Category> category = categoryRepository.findByName(request.getCategoryName());

        if (category.isEmpty()) {
            category = Optional.of(categoryRepository.save(new Category().setName(request.getCategoryName())));
        }

        return new Cashback()
                .setCard(card)
                .setCategory(category.get())
                .setPercent(request.getPercent() / 100.0);
    }

    private Cashback getCashbackEntityForDelete(DeleteCashbackRequest deleteCashbackRequest) {
        Card card = cardService.findCardByName(deleteCashbackRequest.getCardName());

        Category category = categoryRepository.findByName(deleteCashbackRequest.getCategoryName()).get();

        return new Cashback().setCard(card).setCategory(category);
    }
}
