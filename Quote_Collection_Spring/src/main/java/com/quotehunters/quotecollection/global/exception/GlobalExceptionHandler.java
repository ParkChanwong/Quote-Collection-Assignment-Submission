package com.quotehunters.quotecollection.global.exception;

import com.quotehunters.quotecollection.domain.account.exception.*;
import com.quotehunters.quotecollection.domain.mypage.bookmark.exception.NotFoundBookmarkException;
import com.quotehunters.quotecollection.global.common.ResponseError;
import com.quotehunters.quotecollection.domain.category.exception.field.DuplicateFieldException;
import com.quotehunters.quotecollection.domain.category.exception.field.EmptyFieldException;
import com.quotehunters.quotecollection.domain.category.exception.field.NotFoundFieldException;
import com.quotehunters.quotecollection.domain.category.exception.country.DuplicateCountryException;
import com.quotehunters.quotecollection.domain.category.exception.country.EmptyCountryException;
import com.quotehunters.quotecollection.domain.category.exception.country.NotFoundCountryException;
import com.quotehunters.quotecollection.domain.category.exception.period.DuplicatePeriodException;
import com.quotehunters.quotecollection.domain.category.exception.period.EmptyPeriodException;
import com.quotehunters.quotecollection.domain.category.exception.period.NotFoundPeriodException;
import com.quotehunters.quotecollection.domain.category.exception.theme.DuplicateThemeException;
import com.quotehunters.quotecollection.domain.category.exception.theme.EmptyThemeException;
import com.quotehunters.quotecollection.domain.category.exception.theme.NotFoundThemeException;
import com.quotehunters.quotecollection.domain.person.exception.EmptyPersonException;
import com.quotehunters.quotecollection.domain.person.exception.NotFoundPersonException;
import com.quotehunters.quotecollection.domain.quote.exception.DuplicateQuoteException;
import com.quotehunters.quotecollection.domain.quote.exception.EmptyQuoteException;
import com.quotehunters.quotecollection.domain.quote.exception.NotFoundQuoteException;
import com.quotehunters.quotecollection.domain.mypage.bookmark.exception.DuplicateBookmarkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 존재하지 않음
    @ExceptionHandler({
            NotFoundCountryException.class,
            NotFoundPeriodException.class,
            NotFoundFieldException.class,
            NotFoundThemeException.class,
            NotFoundPersonException.class,
            NotFoundQuoteException.class,
            NotFoundBookmarkException.class,
            NotFoundUserException.class
    })
    public ResponseEntity<ResponseError> notFoundException(Exception e) {
        ResponseError responseError = new ResponseError(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );

        return new ResponseEntity<>(responseError, HttpStatus.NOT_FOUND);
    }

    // 필수값을 빈값으로 등록
    @ExceptionHandler({
            EmptyCountryException.class,
            EmptyPeriodException.class,
            EmptyFieldException.class,
            EmptyThemeException.class,
            EmptyPersonException.class,
            EmptyQuoteException.class,
            EmptyIdException.class,
            EmptyPasswordException.class,
            InvalidAuthException.class
    })
    public ResponseEntity<ResponseError> emptyException(Exception e) {
        ResponseError responseError = new ResponseError(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage()
        );

        return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
    }

    // 중복 등록
    @ExceptionHandler({
            DuplicateCountryException.class,
            DuplicatePeriodException.class,
            DuplicateFieldException.class,
            DuplicateThemeException.class,
            DuplicateQuoteException.class,
            DuplicateIdException.class,
            DuplicateBookmarkException.class
    })
    public ResponseEntity<ResponseError> duplicateException(Exception e) {
        ResponseError responseError = new ResponseError(
                HttpStatus.CONFLICT.value(),
                e.getMessage()
        );

        return new ResponseEntity<>(responseError, HttpStatus.CONFLICT);
    }

    // 로그인 실패
    @ExceptionHandler(SignInFailedException.class)
    public ResponseEntity<ResponseError> loginFailed(Exception e) {
        ResponseError responseError = new ResponseError(
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage()
        );

        return new ResponseEntity<>(responseError, HttpStatus.UNAUTHORIZED);
    }

    // 요청 형식 오류
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ResponseError> invalidRequest(Exception e) {
        return ResponseEntity.badRequest().body(new ResponseError(
                HttpStatus.BAD_REQUEST.value(), "필수값과 요청 데이터 형식을 확인해주세요."));
    }

    // 동시 등록 등으로 DB 제약에 걸린 경우에도 내부 SQL은 응답하지 않음
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseError> dataConflict(DataIntegrityViolationException e) {
        log.warn("데이터 제약 위반", e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseError(
                HttpStatus.CONFLICT.value(), "중복 데이터 또는 연관 데이터 제약으로 처리할 수 없습니다."));
    }

    // 잡지 못한 문제 통합
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleException(Exception e){
        log.error("처리하지 못한 서버 오류", e);
        ResponseError responseError = new ResponseError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "서버 내부 오류가 발생했습니다."
        );

        return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
