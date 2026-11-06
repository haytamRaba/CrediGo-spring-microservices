package com.creditgo.credit.repository;

import com.creditgo.credit.entity.RepaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepaymentScheduleRepository extends JpaRepository<RepaymentSchedule, Long> {

    List<RepaymentSchedule> findByLoanApplicationId(Long loanApplicationId);

    Optional<RepaymentSchedule> findByLoanApplicationIdAndNumeroEcheanceOrderByNumeroEcheanceAsc(
            Long loanApplicationId, Integer numeroEcheance);

    boolean existsByLoanApplicationId(Long loanApplicationId);
}
