package com.quotehunters.quotecollection.domain.category.service;

import com.quotehunters.quotecollection.domain.category.dto.CountryDTO;
import com.quotehunters.quotecollection.domain.category.entity.CountryEntity;
import com.quotehunters.quotecollection.domain.category.exception.country.DuplicateCountryException;
import com.quotehunters.quotecollection.domain.category.exception.country.EmptyCountryException;
import com.quotehunters.quotecollection.domain.category.exception.country.NotFoundCountryException;
import com.quotehunters.quotecollection.domain.category.repository.CountryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {
    private static final Sort COUNTRY_SORT = Sort.by("name");
    private final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    private CountryDTO convertToDTO(CountryEntity countryEntity){
        return new CountryDTO(countryEntity.getId(), countryEntity.getName());
    }

    private CountryEntity convertToEntity(CountryDTO countryDTO){
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setName(countryDTO.getName().trim());

        return countryEntity;
    }

    // 전체 국가 조회
    public List<CountryDTO> findAllCountries() {
        // Sort.by로 국가명 가나다 순으로 정렬
        List<CountryEntity> countries = countryRepository.findAll(COUNTRY_SORT);

        return countries.stream().map(this::convertToDTO).toList();
    }

    // 전체 국가 조회 - 페이지네이션
    public Page<CountryDTO> findAllCountriesByPage(Pageable pageable) {
        return countryRepository.findAll(pageable).map(this::convertToDTO);
    }

    // 국가명 조회
    public List<CountryDTO> findAllCountriesByName(String name) {
        List<CountryEntity> countries = countryRepository.findByNameContaining(name, COUNTRY_SORT);

        return countries.stream().map(this::convertToDTO).toList();
    }

    // 국가 id 단일 조회
    public CountryDTO findCountryById(int id){
        CountryEntity countryEntity = countryRepository
                .findById(id)
                .orElseThrow(NotFoundCountryException::new);

        return convertToDTO(countryEntity);
    }

    // 국가 등록
    @Transactional
    public void saveCountry(CountryDTO countryDTO){
        if (countryDTO.getName() == null || countryDTO.getName().isBlank()){
            throw new EmptyCountryException();
        } else if (countryRepository.existsByName(countryDTO.getName().trim())){
            throw new DuplicateCountryException();
        }

        countryRepository.save(convertToEntity(countryDTO));
    }

    // 국가 수정
    @Transactional
    public void modifyCountry(int id, CountryDTO countryDTO){
        CountryEntity findCountry = countryRepository
                .findById(id)
                .orElseThrow(NotFoundCountryException::new);

        if (countryDTO.getName() == null || countryDTO.getName().isBlank()){
            throw new EmptyCountryException();
        } else if (countryRepository.existsByName(countryDTO.getName().trim())){
            throw new DuplicateCountryException();
        }

        findCountry.setName(countryDTO.getName().trim());
    }

    // 국가 삭제
    @Transactional
    public void deleteCountry(int id){
        CountryEntity deleteCountry = countryRepository.findById(id).orElseThrow(NotFoundCountryException::new);

        countryRepository.delete(deleteCountry);
    }
}
