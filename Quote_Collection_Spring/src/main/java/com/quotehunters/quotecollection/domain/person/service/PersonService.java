package com.quotehunters.quotecollection.domain.person.service;

import com.quotehunters.quotecollection.domain.person.dto.PersonRequestDTO;
import com.quotehunters.quotecollection.domain.person.dto.PersonResponseDTO;
import com.quotehunters.quotecollection.domain.category.entity.CountryEntity;
import com.quotehunters.quotecollection.domain.category.entity.FieldEntity;
import com.quotehunters.quotecollection.domain.category.entity.PeriodEntity;
import com.quotehunters.quotecollection.domain.person.entity.PersonEntity;
import com.quotehunters.quotecollection.domain.category.exception.country.NotFoundCountryException;
import com.quotehunters.quotecollection.domain.category.exception.field.NotFoundFieldException;
import com.quotehunters.quotecollection.domain.category.exception.period.NotFoundPeriodException;
import com.quotehunters.quotecollection.domain.person.exception.EmptyPersonException;
import com.quotehunters.quotecollection.domain.person.exception.NotFoundPersonException;
import com.quotehunters.quotecollection.domain.category.repository.CountryRepository;
import com.quotehunters.quotecollection.domain.category.repository.FieldRepository;
import com.quotehunters.quotecollection.domain.category.repository.PeriodRepository;
import com.quotehunters.quotecollection.domain.person.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    private static final Sort PERSON_SORT = Sort.by("name", "countryName", "fieldName", "periodName", "id");

    private final PersonRepository personRepository;
    private final CountryRepository countryRepository;
    private final PeriodRepository periodRepository;
    private final FieldRepository fieldRepository;

    @Autowired
    public PersonService(
            PersonRepository personRepository,
            CountryRepository countryRepository,
            PeriodRepository periodRepository,
            FieldRepository fieldRepository
    ) {
        this.personRepository = personRepository;
        this.countryRepository = countryRepository;
        this.periodRepository = periodRepository;
        this.fieldRepository = fieldRepository;
    }

    private PersonResponseDTO convertToDTO(PersonEntity personEntity) {
        return new PersonResponseDTO(
                personEntity.getId(),
                personEntity.getCountry().getName(),
                personEntity.getPeriod().getName(),
                personEntity.getField().getName(),
                personEntity.getName()
        );
    }

    private CountryEntity findCountryOrThrow(int countryId) {
        return countryRepository.findById(countryId).orElseThrow(NotFoundCountryException::new);
    }

    private PeriodEntity findPeriodOrThrow(int periodId) {
        return periodRepository.findById(periodId).orElseThrow(NotFoundPeriodException::new);
    }

    private FieldEntity findFieldOrThrow(int fieldId) {
        return fieldRepository.findById(fieldId).orElseThrow(NotFoundFieldException::new);
    }

    private PersonEntity convertToEntity(
            PersonRequestDTO personRequestDTO,
            CountryEntity countryEntity,
            PeriodEntity periodEntity,
            FieldEntity fieldEntity
    ) {
        PersonEntity personEntity = new PersonEntity();
        personEntity.setCountry(countryEntity);
        personEntity.setPeriod(periodEntity);
        personEntity.setField(fieldEntity);
        personEntity.setName(personRequestDTO.getName().trim());

        return personEntity;
    }

    // 전체 인물 조회
    public List<PersonResponseDTO> findAllPersons() {
        List<PersonEntity> persons = personRepository.findAll(PERSON_SORT);

        return persons.stream().map(this::convertToDTO).toList();
    }

    // 전체 인물 조회 - 페이지네이션
    public Page<PersonResponseDTO> findAllPersons(Pageable pageable) {
        return personRepository.findAll(pageable).map(this::convertToDTO);
    }


    // 국가명으로 인물 조회
    public List<PersonResponseDTO> findAllPersonsByCountryName(String countryName) {
        List<PersonEntity> persons = personRepository.findByCountryNameContaining(countryName, PERSON_SORT);

        return persons.stream().map(this::convertToDTO).toList();
    }

    // 시대명으로 인물 조회
    public List<PersonResponseDTO> findAllPersonsByPeriodName(String periodName) {
        List<PersonEntity> persons = personRepository.findByPeriodNameContaining(periodName, PERSON_SORT);

        return persons.stream().map(this::convertToDTO).toList();
    }

    // 분야명으로 인물 조회
    public List<PersonResponseDTO> findAllPersonsByFieldName(String fieldName) {
        List<PersonEntity> persons = personRepository.findByFieldNameContaining(fieldName, PERSON_SORT);

        return persons.stream().map(this::convertToDTO).toList();
    }

    // 인물명으로 인물 조회
    public List<PersonResponseDTO> findAllPersonsByName(String name) {
        List<PersonEntity> persons = personRepository.findByNameContaining(name, PERSON_SORT);

        return persons.stream().map(this::convertToDTO).toList();
    }

    // 인물명 ID 단일 조회
    public PersonResponseDTO findPersonById(int id) {
        PersonEntity person = personRepository
                .findById(id)
                .orElseThrow(NotFoundPersonException::new);

        return convertToDTO(person);
    }

    // 인물 등록
    @Transactional
    public void savePerson(PersonRequestDTO personDTO) {
        CountryEntity country = findCountryOrThrow(personDTO.getCountryId());
        PeriodEntity period = findPeriodOrThrow(personDTO.getPeriodId());
        FieldEntity field = findFieldOrThrow(personDTO.getFieldId());

        if (personDTO.getName() == null || personDTO.getName().isBlank()) {
            throw new EmptyPersonException();
        }

        personRepository.save(convertToEntity(personDTO, country, period, field));
    }

    // 인물 수정
    @Transactional
    public void modifyPerson(int id, PersonRequestDTO personDTO) {
        PersonEntity person = personRepository
                .findById(id)
                .orElseThrow(NotFoundPersonException::new);

        CountryEntity country = findCountryOrThrow(personDTO.getCountryId());
        PeriodEntity period = findPeriodOrThrow(personDTO.getPeriodId());
        FieldEntity field = findFieldOrThrow(personDTO.getFieldId());

        if (personDTO.getName() == null || personDTO.getName().isBlank()) {
            throw new EmptyPersonException();
        }

        person.setCountry(country);
        person.setPeriod(period);
        person.setField(field);
        person.setName(personDTO.getName().trim());
    }

    // 인물 삭제
    @Transactional
    public void deletePerson(int id) {
        PersonEntity person = personRepository
                .findById(id)
                .orElseThrow(NotFoundPersonException::new);

        personRepository.delete(person);
    }
}
