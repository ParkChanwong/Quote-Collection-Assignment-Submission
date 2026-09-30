package com.quotehunters.quotecollection.domain.category.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.quotehunters.quotecollection.global.common.ResponseList;
import com.quotehunters.quotecollection.global.common.ResponsePage;
import com.quotehunters.quotecollection.global.common.ResponseSingle;
import com.quotehunters.quotecollection.domain.category.dto.FieldDTO;
import com.quotehunters.quotecollection.domain.category.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "분야", description = "분야 조회·등록·수정·삭제 API")
@RestController
@RequestMapping("/field")
public class FieldController {
    private final FieldService fieldService;

    @Autowired
    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    // 전체 분야 조회
    @Operation(summary = "분야 목록 조회",
            description = "page를 생략하면 전체 분야 목록을, 입력하면 페이지 단위로 조회합니다. 페이지 번호는 1부터 시작하며 size로 페이지 크기를 지정합니다.")
    @GetMapping(params = "!page")
    public ResponseEntity<ResponseList<FieldDTO>> findAllFields() {
        List<FieldDTO> fields = fieldService.findAllFields();

        return ResponseEntity.ok(new ResponseList<>(HttpStatus.OK.value(), fields));
    }

    // 전체 분야 조회 - 페이지네이션
    @Operation(summary = "분야 목록 조회",
            description = "page를 생략하면 전체 분야 목록을, 입력하면 페이지 단위로 조회합니다. 페이지 번호는 1부터 시작하며 size로 페이지 크기를 지정합니다.")
    @GetMapping(params = "page")
    public ResponseEntity<ResponsePage<FieldDTO>> findAllFieldsByPage(@ParameterObject Pageable pageable) {
        Page<FieldDTO> fields = fieldService.findAllFieldsByPage(pageable);

        return ResponseEntity.ok(new ResponsePage<>(
                HttpStatus.OK.value(),
                fields.getContent(),
                fields.getTotalElements(),
                fields.getTotalPages()
        ));
    }

    // 분야명으로 분야 조회
    @Operation(summary = "분야명으로 분야 검색",
            description = "keyword로 분야명을 검색하여 분야 목록을 조회합니다. 로그인 없이 요청할 수 있습니다.")
    @GetMapping("/name")
    public ResponseEntity<ResponseList<FieldDTO>> findAllFieldsByName(@RequestParam String keyword) {
        List<FieldDTO> fields = fieldService.findAllFieldsByName(keyword.trim());

        return ResponseEntity.ok(new ResponseList<>(HttpStatus.OK.value(), fields));
    }

    // 분야 ID 단일 조회
    @Operation(summary = "분야 단일 조회",
            description = "ID로 분야 정보를 조회합니다. 로그인 없이 요청할 수 있습니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseSingle<FieldDTO>> findFieldById(@PathVariable int id) {
        FieldDTO fieldDTO = fieldService.findFieldById(id);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), fieldDTO));
    }

    // 분야 등록
    @Operation(summary = "분야 등록",
            description = "분야 정보를 등록합니다. 관리자(ADMIN) JWT가 필요합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<ResponseSingle<String>> saveField(@RequestBody FieldDTO fieldDTO) {
        fieldService.saveField(fieldDTO);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "분야 등록 성공"));
    }

    // 분야 수정
    @Operation(summary = "분야 수정",
            description = "분야 정보를 수정합니다. 관리자(ADMIN) JWT가 필요합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseSingle<String>> modifyField(@PathVariable int id, @RequestBody FieldDTO fieldDTO) {
        fieldService.modifyField(id, fieldDTO);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "분야 수정 성공"));
    }

    // 분야 삭제
    @Operation(summary = "분야 삭제",
            description = "분야 정보를 삭제합니다. 관리자(ADMIN) JWT가 필요합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseSingle<String>> deleteField(@PathVariable int id) {
        fieldService.deleteField(id);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "분야 삭제 성공"));
    }
}
