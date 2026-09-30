package com.quotehunters.quotecollection.domain.category.service;

import com.quotehunters.quotecollection.domain.category.dto.PeriodDTO;
import com.quotehunters.quotecollection.domain.category.entity.PeriodEntity;
import com.quotehunters.quotecollection.domain.category.exception.period.DuplicatePeriodException;
import com.quotehunters.quotecollection.domain.category.exception.period.EmptyPeriodException;
import com.quotehunters.quotecollection.domain.category.exception.period.NotFoundPeriodException;
import com.quotehunters.quotecollection.domain.category.repository.PeriodRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeriodService {
    private static final Sort PERIOD_SORT = Sort.by("name");

    private final PeriodRepository periodRepository;

    @Autowired
    public PeriodService(PeriodRepository periodRepository) {
        this.periodRepository = periodRepository;
    }

    private PeriodDTO convertToDTO(PeriodEntity periodEntity) {
        return new PeriodDTO(periodEntity.getId(), periodEntity.getName());
    }

    private PeriodEntity convertToEntity(PeriodDTO periodDTO) {
        PeriodEntity periodEntity = new PeriodEntity();
        periodEntity.setName(periodDTO.getName().trim());

        return periodEntity;
    }

    // 전체 시대 조회
    public List<PeriodDTO> findAllPeriods() {
        List<PeriodEntity> periods = periodRepository.findAll(PERIOD_SORT);

        return periods.stream().map(this::convertToDTO).toList();
    }

    // 전체 시대 조회 - 페이지네이션
    public Page<PeriodDTO> findAllPeriodsByPage(Pageable pageable) {
        return periodRepository.findAll(pageable).map(this::convertToDTO);
    }

    // 시대명으로 시대 조회
    public List<PeriodDTO> findAllPeriodsByName(String name) {
        List<PeriodEntity> periods = periodRepository.findByNameContaining(name, PERIOD_SORT);

        return periods.stream().map(this::convertToDTO).toList();
    }

    // 시대 ID 단일 조회
    public PeriodDTO findPeriodById(int id) {
        PeriodEntity period =  periodRepository
                .findById(id)
                .orElseThrow(NotFoundPeriodException::new);

        return convertToDTO(period);
    }

    // 시대 등록
    @Transactional
    public void savePeriod(PeriodDTO periodDTO) {
        if (periodDTO.getName() == null || periodDTO.getName().isBlank()) {
            throw new EmptyPeriodException();
        } else if (periodRepository.existsByName(periodDTO.getName().trim())) {
            throw new DuplicatePeriodException();
        }

        periodRepository.save(convertToEntity(periodDTO));
    }

    // 시대 수정
    @Transactional
    public void modifyPeriod(int id, PeriodDTO periodDTO) {
        PeriodEntity period = periodRepository
                .findById(id)
                .orElseThrow(NotFoundPeriodException::new);
        if (periodDTO.getName() == null || periodDTO.getName().isBlank()) {
            throw new EmptyPeriodException();
        } else if (periodRepository.existsByName(periodDTO.getName().trim())) {
            throw new DuplicatePeriodException();
        }

        period.setName(periodDTO.getName().trim());
    }

    // 시대 삭제
    @Transactional
    public void deletePeriod(int id) {
        PeriodEntity period = periodRepository
                .findById(id)
                .orElseThrow(NotFoundPeriodException::new);

        periodRepository.delete(period);
    }
}
