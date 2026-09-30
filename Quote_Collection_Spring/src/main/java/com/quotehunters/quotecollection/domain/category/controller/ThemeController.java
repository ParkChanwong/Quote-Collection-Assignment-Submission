package com.quotehunters.quotecollection.domain.category.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.quotehunters.quotecollection.global.common.ResponseList;
import com.quotehunters.quotecollection.global.common.ResponsePage;
import com.quotehunters.quotecollection.global.common.ResponseSingle;
import com.quotehunters.quotecollection.domain.category.dto.ThemeDTO;
import com.quotehunters.quotecollection.domain.category.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "주제", description = "주제 조회·등록·수정·삭제 API")
@RestController
@RequestMapping("/theme")
public class ThemeController {
    private final ThemeService themeService;

    @Autowired
    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    // 전체 주제 조회
    @Operation(summary = "주제 목록 조회",
            description = "page를 생략하면 전체 주제 목록을, 입력하면 페이지 단위로 조회합니다. 페이지 번호는 1부터 시작하며 size로 페이지 크기를 지정합니다.")
    @GetMapping(params = "!page")
    public ResponseEntity<ResponseList<ThemeDTO>> findAllThemes() {
        List<ThemeDTO> themes = themeService.findAllThemes();

        return ResponseEntity.ok(new ResponseList<>(HttpStatus.OK.value(), themes));
    }

    // 전체 주제 조회 - 페이지네이션
    @Operation(summary = "주제 목록 조회",
            description = "page를 생략하면 전체 주제 목록을, 입력하면 페이지 단위로 조회합니다. 페이지 번호는 1부터 시작하며 size로 페이지 크기를 지정합니다.")
    @GetMapping(params = "page")
    public ResponseEntity<ResponsePage<ThemeDTO>> findAllThemesByPage(
            @PageableDefault(
                    size = 10,
                    sort = {"name"}
            )
            @ParameterObject Pageable pageable
    ) {
        Page<ThemeDTO> themes = themeService.findAllThemesByPage(pageable);

        return ResponseEntity.ok(new ResponsePage<>(
                HttpStatus.OK.value(),
                themes.getContent(),
                themes.getTotalElements(),
                themes.getTotalPages()
        ));
    }

    // 주제명으로 주제 조회
    @Operation(summary = "주제명으로 주제 검색",
            description = "keyword로 주제명을 검색하여 주제 목록을 조회합니다. 로그인 없이 요청할 수 있습니다.")
    @GetMapping("/name")
    public ResponseEntity<ResponseList<ThemeDTO>> findAllThemesByName(@RequestParam String keyword) {
        List<ThemeDTO> themes = themeService.findAllThemesByName(keyword.trim());

        return ResponseEntity.ok(new ResponseList<>(HttpStatus.OK.value(), themes));
    }

    // 주제 ID 단일 조회
    @Operation(summary = "주제 단일 조회",
            description = "ID로 주제 정보를 조회합니다. 로그인 없이 요청할 수 있습니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseSingle<ThemeDTO>> findThemeById(@PathVariable int id) {
        ThemeDTO theme = themeService.findThemeById(id);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), theme));
    }

    // 주제 등록
    @Operation(summary = "주제 등록",
            description = "주제 정보를 등록합니다. 관리자(ADMIN) JWT가 필요합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<ResponseSingle<String>> saveTheme(@RequestBody ThemeDTO theme) {
        themeService.saveTheme(theme);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "주제 등록 성공"));
    }

    // 주제 수정
    @Operation(summary = "주제 수정",
            description = "주제 정보를 수정합니다. 관리자(ADMIN) JWT가 필요합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseSingle<String>> modifyTheme(@PathVariable int id, @RequestBody ThemeDTO theme) {
        themeService.modifyTheme(id, theme);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "주제 수정 성공"));
    }

    // 주제 삭제
    @Operation(summary = "주제 삭제",
            description = "주제 정보를 삭제합니다. 관리자(ADMIN) JWT가 필요합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseSingle<String>> deleteTheme(@PathVariable int id) {
        themeService.deleteTheme(id);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "주제 삭제 성공"));
    }
}
