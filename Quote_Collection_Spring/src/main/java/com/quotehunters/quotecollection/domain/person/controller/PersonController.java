package com.quotehunters.quotecollection.domain.person.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.quotehunters.quotecollection.global.common.ResponseList;
import com.quotehunters.quotecollection.global.common.ResponsePage;
import com.quotehunters.quotecollection.global.common.ResponseSingle;
import com.quotehunters.quotecollection.domain.person.dto.PersonRequestDTO;
import com.quotehunters.quotecollection.domain.person.dto.PersonResponseDTO;
import com.quotehunters.quotecollection.domain.person.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "인물", description = "인물 조회·검색·등록·수정·삭제 API")
@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    // 전체 인물 조회
    @Operation(summary = "인물 목록 조회",
            description = "page를 생략하면 전체 인물 목록을, 입력하면 페이지 단위로 조회합니다. 페이지 번호는 1부터 시작하며 size로 페이지 크기를 지정합니다.")
    @GetMapping(params = "!page")
    public ResponseEntity<ResponseList<PersonResponseDTO>> findAllPersons() {
        List<PersonResponseDTO> persons = personService.findAllPersons();

        return ResponseEntity.ok(new ResponseList<>(HttpStatus.OK.value(), persons));
    }

    // 전체 인물 조회 - 페이지네이션
    @Operation(summary = "인물 목록 조회",
            description = "page를 생략하면 전체 인물 목록을, 입력하면 페이지 단위로 조회합니다. 페이지 번호는 1부터 시작하며 size로 페이지 크기를 지정합니다.")
    @GetMapping(params = "page")
    public ResponseEntity<ResponsePage<PersonResponseDTO>> findAllPersonsByPage (
            @PageableDefault(
                    size = 10,
                    sort = {"name", "countryName", "fieldName", "periodName", "id"}
            )
            @ParameterObject Pageable pageable
    ) {
        Page<PersonResponseDTO> persons = personService.findAllPersons(pageable);

        return ResponseEntity.ok(new ResponsePage<>(
                HttpStatus.OK.value(),
                persons.getContent(),
                persons.getTotalElements(),
                persons.getTotalPages()
        ));
    }

    // 국가명으로 인물 조회
    @Operation(summary = "국가명으로 인물 검색",
            description = "keyword로 국가명을 검색하여 인물 목록을 조회합니다. 로그인 없이 요청할 수 있습니다.")
    @GetMapping("/country")
    public ResponseEntity<ResponseList<PersonResponseDTO>> findPersonsByCountry(@RequestParam String keyword) {
        List<PersonResponseDTO> persons = personService.findAllPersonsByCountryName(keyword.trim());

        return ResponseEntity.ok(new ResponseList<>(HttpStatus.OK.value(), persons));
    }

    // 시대명으로 인물 조회
    @Operation(summary = "시대명으로 인물 검색",
            description = "keyword로 시대명을 검색하여 인물 목록을 조회합니다. 로그인 없이 요청할 수 있습니다.")
    @GetMapping("/period")
    public ResponseEntity<ResponseList<PersonResponseDTO>> findPersonsByPeriod(@RequestParam String keyword) {
        List<PersonResponseDTO> persons = personService.findAllPersonsByPeriodName(keyword.trim());

        return ResponseEntity.ok(new ResponseList<>(HttpStatus.OK.value(), persons));
    }

    // 분야명으로 인물 조회
    @Operation(summary = "분야명으로 인물 검색",
            description = "keyword로 분야명을 검색하여 인물 목록을 조회합니다. 로그인 없이 요청할 수 있습니다.")
    @GetMapping("/field")
    public ResponseEntity<ResponseList<PersonResponseDTO>> findPersonsByField(@RequestParam String keyword) {
        List<PersonResponseDTO> persons = personService.findAllPersonsByFieldName(keyword.trim());

        return ResponseEntity.ok(new ResponseList<>(HttpStatus.OK.value(), persons));
    }

    // 인물명으로 인물 조회
    @Operation(summary = "인물명으로 인물 검색",
            description = "keyword로 인물명을 검색하여 인물 목록을 조회합니다. 로그인 없이 요청할 수 있습니다.")
    @GetMapping("/name")
    public ResponseEntity<ResponseList<PersonResponseDTO>> findPersonsByName(@RequestParam String keyword) {
        List<PersonResponseDTO> persons = personService.findAllPersonsByName(keyword.trim());

        return ResponseEntity.ok(new ResponseList<>(HttpStatus.OK.value(), persons));
    }

    // 인물 ID 단일 조회
    @Operation(summary = "인물 단일 조회",
            description = "ID로 인물 정보를 조회합니다. 로그인 없이 요청할 수 있습니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseSingle<PersonResponseDTO>> findPersonById(@PathVariable int id) {
        PersonResponseDTO person = personService.findPersonById(id);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), person));
    }

    // 인물 등록
    @Operation(summary = "인물 등록",
            description = "인물 정보를 등록합니다. 관리자(ADMIN) JWT가 필요합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<ResponseSingle<String>> savePerson(@RequestBody PersonRequestDTO person) {
        personService.savePerson(person);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "인물 등록 성공"));
    }

    // 인물 수정
    @Operation(summary = "인물 수정",
            description = "인물 정보를 수정합니다. 관리자(ADMIN) JWT가 필요합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseSingle<String>> updatePerson(
            @PathVariable int id,
            @RequestBody PersonRequestDTO person
    ) {
        personService.modifyPerson(id, person);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "인물 수정 성공"));
    }

    // 인물 삭제
    @Operation(summary = "인물 삭제",
            description = "인물 정보를 삭제합니다. 관리자(ADMIN) JWT가 필요합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseSingle<String>> deletePerson(@PathVariable int id) {
        personService.deletePerson(id);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "인물 삭제 성공"));
    }
}
