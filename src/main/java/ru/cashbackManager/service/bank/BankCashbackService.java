package ru.cashbackManager.service.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.cashbackManager.dto.bank.AddBankRequest;
import ru.cashbackManager.exception.BankAlreadyExistsException;
import ru.cashbackManager.exception.BankDoNotExistsException;
import ru.cashbackManager.model.Bank;
import ru.cashbackManager.repository.BankRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankCashbackService implements BankService {

    private final BankRepository bankRepository;


    @Override
    public Bank addBank(AddBankRequest request) {
        try {
            return bankRepository.saveAndFlush(
                    new Bank()
                            .setName(request.getBankName())
                            .setLimitAmount(request.getLimit() >= 0 ? request.getLimit() : Long.MAX_VALUE)
            );
        } catch (DataIntegrityViolationException exception) {
            throw new BankAlreadyExistsException();
        }
    }

    @Override
    public Bank findBankByName(String bankName) {
        return bankRepository.findByName(bankName).orElseThrow(BankDoNotExistsException::new);
    }

    @Override
    public List<Bank> getAllBanks() {
        return bankRepository.findAll();
    }
}
