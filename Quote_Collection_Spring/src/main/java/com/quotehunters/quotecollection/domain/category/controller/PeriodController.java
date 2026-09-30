package com.quotehunters.quotecollection.domain.category.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.quotehunters.quotecollection.global.common.ResponseList;
import com.quotehunters.quotecollection.global.common.ResponsePage;
import com.quotehunters.quotecollection.global.common.ResponseSingle;
import com.quotehunters.quotecollection.domain.category.dto.PeriodDTO;
import com.quotehunters.quotecollection.domain.category.service.PeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "시대", description = "시대 조회·등록·수정·삭제 API")
@RestController
@RequestMapping("/period")
public class PeriodController {
    private final PeriodService periodService;

    @Autowired
    public PeriodController(PeriodService periodService) {
        this.periodService = periodService;
    }

    // 전체 시대 조회
    @Operation(summary = "시대 목록 조회",
            description = "page를 생략하면 전체 시대 목록을, 입력하면 페이지 단위로 조회합니다. 페이지 번호는 1부터 시작하며 size로 페이지 크기를 지정합니다.")
    @GetMapping(params = "!page")
    public ResponseEntity<ResponseList<PeriodDTO>> findAllPeriods() {
        List<PeriodDTO> periods = periodService.findAllPeriods();

        return ResponseEntity.ok(new ResponseList<>(HttpStatus.OK.value(), periods));
    }

    // 전체 시대 조회 - 페이지네이션
    @Operation(summary = "시대 목록 조회",
            description = "page를 생략하면 전체 시대 목록을, 입력하면 페이지 단위로 조회합니다. 페이지 번호는 1부터 시작하며 size로 페이지 크기를 지정합니다.")
    @GetMapping(params = "page")
    public ResponseEntity<ResponsePage<PeriodDTO>> findAllPeriodsByPage(
            @PageableDefault(
                    size = 10,
                    sort = {"name"}
            )
            @ParameterObject Pageable pageable
    ) {
        Page<PeriodDTO> periods = periodService.findAllPeriodsByPage(pageable);

        return ResponseEntity.ok(new ResponsePage<>(
                HttpStatus.OK.value(),
                periods.getContent(),
                periods.getTotalElements(),
                periods.getTotalPages()
        ));
    }

    // 시대명으로 시대 조회
    @Operation(summary = "시대명으로 시대 검색",
            description = "keyword로 시대명을 검색하여 시대 목록을 조회합니다. 로그인 없이 요청할 수 있습니다.")
    @GetMapping("/name")
    public ResponseEntity<ResponseList<PeriodDTO>> findPeriodByName(@RequestParam String keyword) {
        List<PeriodDTO> periods = periodService.findAllPeriodsByName(keyword.trim());

        return ResponseEntity.ok(new ResponseList<>(HttpStatus.OK.value(), periods));
    }

    // 시대 ID 단일 조회
    @Operation(summary = "시대 단일 조회",
            description = "ID로 시대 정보를 조회합니다. 로그인 없이 요청할 수 있습니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseSingle<PeriodDTO>> findPeriodById(@PathVariable int id) {
        PeriodDTO periodDTO = periodService.findPeriodById(id);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), periodDTO));
    }

    // 시대 등록
    @Operation(summary = "시대 등록",
            description = "시대 정보를 등록합니다. 관리자(ADMIN) JWT가 필요합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<ResponseSingle<String>> createPeriod(@RequestBody PeriodDTO periodDTO) {
        periodService.savePeriod(periodDTO);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "시대 등록 성공"));
    }

    // 시대 수정
    @Operation(summary = "시대 수정",
            description = "시대 정보를 수정합니다. 관리자(ADMIN) JWT가 필요합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseSingle<String>> updatePeriod(@PathVariable int id, @RequestBody PeriodDTO periodDTO) {
        periodService.modifyPeriod(id, periodDTO);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "시대 수정 성공"));
    }

    // 시대 삭제
    @Operation(summary = "시대 삭제",
            description = "시대 정보를 삭제합니다. 관리자(ADMIN) JWT가 필요합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseSingle<String>> deletePeriod(@PathVariable int id) {
        periodService.deletePeriod(id);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "시대 삭제 성공"));
    }
}
