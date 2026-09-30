package com.quotehunters.quotecollection.domain.category.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.quotehunters.quotecollection.global.common.ResponseList;
import com.quotehunters.quotecollection.global.common.ResponsePage;
import com.quotehunters.quotecollection.global.common.ResponseSingle;
import com.quotehunters.quotecollection.domain.category.dto.CountryDTO;
import com.quotehunters.quotecollection.domain.category.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "국가", description = "국가 조회·등록·수정·삭제 API")
@RestController
@RequestMapping("/country")
public class CountryController {
    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    // 전체 국가 조회
    @Operation(summary = "국가 목록 조회",
            description = "page를 생략하면 전체 국가 목록을, 입력하면 페이지 단위로 조회합니다. 페이지 번호는 1부터 시작하며 size로 페이지 크기를 지정합니다.")
    @GetMapping(params = "!page")
    public ResponseEntity<ResponseList<CountryDTO>> findAllCountries() {
        List<CountryDTO> countries = countryService.findAllCountries();

        return ResponseEntity.ok(new ResponseList<>(HttpStatus.OK.value(), countries));
    }

    // 전체 국가 조회 - 페이지네이션
    @Operation(summary = "국가 목록 조회",
            description = "page를 생략하면 전체 국가 목록을, 입력하면 페이지 단위로 조회합니다. 페이지 번호는 1부터 시작하며 size로 페이지 크기를 지정합니다.")
    @GetMapping(params = "page")
    public ResponseEntity<ResponsePage<CountryDTO>> findAllCountriesByPage(
            @PageableDefault(
                    size = 10,
                    sort = {"name"}
            ) @ParameterObject Pageable page
    ) {
        Page<CountryDTO> countries = countryService.findAllCountriesByPage(page);

        return ResponseEntity.ok(new ResponsePage<>(
                HttpStatus.OK.value(),
                countries.getContent(),
                countries.getTotalElements(),
                countries.getTotalPages()
        ));
    }

    // 국가명으로 국가 조회
    @Operation(summary = "국가명으로 국가 검색",
            description = "keyword로 국가명을 검색하여 국가 목록을 조회합니다. 로그인 없이 요청할 수 있습니다.")
    @GetMapping("/name")
    public ResponseEntity<ResponseList<CountryDTO>> findAllCountriesByName(@RequestParam String keyword) {
        List<CountryDTO> countries = countryService.findAllCountriesByName(keyword.trim());

        return ResponseEntity.ok(new ResponseList<>(HttpStatus.OK.value(), countries));
    }

    // 국가 id 단일 조회
    @Operation(summary = "국가 단일 조회",
            description = "ID로 국가 정보를 조회합니다. 로그인 없이 요청할 수 있습니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseSingle<CountryDTO>> findCountryById(@PathVariable int id){
        CountryDTO countryDTO = countryService.findCountryById(id);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), countryDTO));
    }

    // 국가 등록
    @Operation(summary = "국가 등록",
            description = "국가 정보를 등록합니다. 관리자(ADMIN) JWT가 필요합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<ResponseSingle<String>> updateCountry(@RequestBody CountryDTO countryDTO){
        countryService.saveCountry(countryDTO);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "국가 등록 성공"));
    }

    // 국가 수정
    @Operation(summary = "국가 수정",
            description = "국가 정보를 수정합니다. 관리자(ADMIN) JWT가 필요합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseSingle<String>> updateCountry(@PathVariable int id, @RequestBody CountryDTO countryDTO){
        countryService.modifyCountry(id, countryDTO);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "국가 수정 완료"));
    }

    @Operation(summary = "국가 삭제",
            description = "국가 정보를 삭제합니다. 관리자(ADMIN) JWT가 필요합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseSingle<String>> deleteCountry(@PathVariable int id){
        countryService.deleteCountry(id);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "국가 삭제 완료"));
    }
}
