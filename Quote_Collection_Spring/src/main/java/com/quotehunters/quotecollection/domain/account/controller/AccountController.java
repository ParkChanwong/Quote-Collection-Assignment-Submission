package com.quotehunters.quotecollection.domain.account.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import com.quotehunters.quotecollection.domain.account.dto.AccountDTO;
import com.quotehunters.quotecollection.domain.account.service.AccountService;
import com.quotehunters.quotecollection.global.common.ResponseSingle;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원", description = "회원가입·로그인 API")
@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // 로그인
    @Operation(summary = "로그인",
            description = "아이디와 비밀번호로 로그인하고 JWT를 발급합니다. auth는 일반 회원 1(기본값), 관리자 0입니다.")
    @PostMapping("/signin")
    public ResponseEntity<ResponseSingle<String>> signIn(
            @RequestBody AccountDTO accountDTO,
            @RequestParam(defaultValue = "1") int auth
    ) {
        String token = accountService.signIn(accountDTO, auth);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), token));
    }

    // 유저 회원가입
    @Operation(summary = "유저 회원가입",
            description = "일반 회원 계정을 생성합니다. 로그인 없이 요청할 수 있습니다.")
    @PostMapping("/signup/user")
    public ResponseEntity<ResponseSingle<String>> userSignUp(@RequestBody AccountDTO accountDTO) {
        accountService.userSignUp(accountDTO);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "회원가입 성공"));
    }

    // 관리자 회원가입
    @Operation(summary = "관리자 회원가입",
            description = "관리자 계정을 생성합니다. 현재 보안 설정에서는 로그인 없이 요청할 수 있습니다.")
    @PostMapping("/signup/admin")
    public ResponseEntity<ResponseSingle<String>> adminSignUp(@RequestBody AccountDTO accountDTO) {
        accountService.adminSignUp(accountDTO);

        return ResponseEntity.ok(new ResponseSingle<>(HttpStatus.OK.value(), "회원가입 성공"));
    }
}
