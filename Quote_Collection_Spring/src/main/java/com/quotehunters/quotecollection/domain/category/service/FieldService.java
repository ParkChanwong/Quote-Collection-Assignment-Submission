package com.quotehunters.quotecollection.domain.category.service;

import com.quotehunters.quotecollection.domain.category.dto.FieldDTO;
import com.quotehunters.quotecollection.domain.category.entity.FieldEntity;
import com.quotehunters.quotecollection.domain.category.exception.field.DuplicateFieldException;
import com.quotehunters.quotecollection.domain.category.exception.field.EmptyFieldException;
import com.quotehunters.quotecollection.domain.category.exception.field.NotFoundFieldException;
import com.quotehunters.quotecollection.domain.category.repository.FieldRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FieldService {
    private static final Sort FIELD_SORT = Sort.by("name");

    private final FieldRepository fieldRepository;

    @Autowired
    public FieldService(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    private FieldDTO convertToDTO(FieldEntity fieldEntity) {
        return new FieldDTO(fieldEntity.getId(), fieldEntity.getName());
    }

    private FieldEntity convertToEntity(FieldDTO fieldDTO) {
        FieldEntity fieldEntity = new FieldEntity();
        fieldEntity.setName(fieldDTO.getName().trim());

        return fieldEntity;
    }

    // 전체 분야 조회
    public List<FieldDTO> findAllFields() {
        List<FieldEntity> fields = fieldRepository.findAll(FIELD_SORT);

        return fields.stream().map(this::convertToDTO).toList();
    }

    // 전체 분야 조회 - 페이지네이션
    public Page<FieldDTO> findAllFieldsByPage(Pageable pageable) {
        return fieldRepository.findAll(pageable).map(this::convertToDTO);
    }


    // 분야명으로 분야 조회
    public List<FieldDTO> findAllFieldsByName(String name) {
        List<FieldEntity> fields = fieldRepository.findByNameContaining(name, FIELD_SORT);

        return fields.stream().map(this::convertToDTO).toList();
    }

    // 분야 ID 단일 조회
    public FieldDTO findFieldById(int id) {
        FieldEntity fieldEntity = fieldRepository
                .findById(id)
                .orElseThrow(NotFoundFieldException::new);

        return convertToDTO(fieldEntity);
    }

    // 분야 등록
    @Transactional
    public void saveField(FieldDTO fieldDTO) {
        if (fieldDTO.getName() == null || fieldDTO.getName().isBlank()) {
            throw new EmptyFieldException();
        } else if (fieldRepository.existsByName(fieldDTO.getName().trim())) {
            throw new DuplicateFieldException();
        }

        fieldRepository.save(convertToEntity(fieldDTO));
    }

    // 분야 수정
    @Transactional
    public void modifyField(int id, FieldDTO fieldDTO) {
        FieldEntity field = fieldRepository
                .findById(id)
                .orElseThrow(NotFoundFieldException::new);

        if (fieldDTO.getName() == null || fieldDTO.getName().isBlank()) {
            throw new EmptyFieldException();
        } else if (fieldRepository.existsByName(fieldDTO.getName().trim())) {
            throw new DuplicateFieldException();
        }

        field.setName(fieldDTO.getName().trim());
    }

    // 분야 삭제
    @Transactional
    public void deleteField(int id) {
        FieldEntity field = fieldRepository
                .findById(id)
                .orElseThrow(NotFoundFieldException::new);

        fieldRepository.delete(field);
    }
}
