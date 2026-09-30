package com.quotehunters.quotecollection.domain.category.service;

import com.quotehunters.quotecollection.domain.category.dto.ThemeDTO;
import com.quotehunters.quotecollection.domain.category.entity.ThemeEntity;
import com.quotehunters.quotecollection.domain.category.exception.theme.DuplicateThemeException;
import com.quotehunters.quotecollection.domain.category.exception.theme.EmptyThemeException;
import com.quotehunters.quotecollection.domain.category.exception.theme.NotFoundThemeException;
import com.quotehunters.quotecollection.domain.category.repository.ThemeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeService {
    private static final Sort THEME_SORT = Sort.by("name");

    private final ThemeRepository themeRepository;

    @Autowired
    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    private ThemeDTO convertToDTO(ThemeEntity themeEntity) {
        return new ThemeDTO(themeEntity.getId(), themeEntity.getName());
    }

    private ThemeEntity convertToEntity(ThemeDTO themeDTO) {
        ThemeEntity themeEntity = new ThemeEntity();
        themeEntity.setName(themeDTO.getName().trim());

        return themeEntity;
    }

    // 전체 주제 조회
    public List<ThemeDTO> findAllThemes() {
        List<ThemeEntity> themes = themeRepository.findAll(THEME_SORT);

        return themes.stream().map(this::convertToDTO).toList();
    }

    // 전체 주제 조회 - 페이지네이션
    public Page<ThemeDTO> findAllThemesByPage(Pageable pageable) {
        return themeRepository.findAll(pageable).map(this::convertToDTO);
    }

    // 주제명으로 주제 조회
    public List<ThemeDTO> findAllThemesByName(String name) {
        List<ThemeEntity> themes = themeRepository.findByNameContaining(name, THEME_SORT);

        return themes.stream().map(this::convertToDTO).toList();
    }

    // 주제 ID 단일 조회
    public ThemeDTO findThemeById(int id) {
        ThemeEntity theme = themeRepository
                .findById(id)
                .orElseThrow(NotFoundThemeException::new);

        return convertToDTO(theme);
    }

    // 주제 등록
    @Transactional
    public void saveTheme(ThemeDTO themeDTO) {
        if (themeDTO.getName() == null || themeDTO.getName().isBlank()) {
            throw new EmptyThemeException();
        } else if (themeRepository.existsByName(themeDTO.getName().trim())) {
            throw new DuplicateThemeException();
        }

        themeRepository.save(convertToEntity(themeDTO));
    }

    // 주제 수정
    @Transactional
    public void modifyTheme(int id, ThemeDTO themeDTO) {
        ThemeEntity theme = themeRepository
                .findById(id)
                .orElseThrow(NotFoundThemeException::new);

        if (themeDTO.getName() == null || themeDTO.getName().isBlank()) {
            throw new EmptyThemeException();
        } else if (themeRepository.existsByName(themeDTO.getName().trim())) {
            throw new DuplicateThemeException();
        }

        theme.setName(themeDTO.getName().trim());
    }

    // 주제 삭제
    @Transactional
    public void deleteTheme(int id) {
        ThemeEntity theme = themeRepository
                .findById(id)
                .orElseThrow(NotFoundThemeException::new);

        themeRepository.delete(theme);
    }
}
