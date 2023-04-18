package com.scaling.libraryservice.service;

import com.scaling.libraryservice.dto.LoanItemDto;
import com.scaling.libraryservice.entity.LoanItem;
import com.scaling.libraryservice.repository.LoanItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApiDataManageService {

    private final LoanItemRepository loanItemRepo;

    @Transactional
    public void addLoanItem(LoanItemDto loanItemDto){

        loanItemRepo.save(new LoanItem(loanItemDto));
    }

    @Transactional
    public void addLoanItemList(List<LoanItemDto> dtoList){

        dtoList.forEach(d -> addLoanItem(d));
    }

}
