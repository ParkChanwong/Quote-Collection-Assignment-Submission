# 명언 수집 어드민 — 에이전트 지침

현재 어드민의 디자인 토큰으로 화면을 만들고, 데이터는 Quote Collection Spring REST API 서버에서 가져온다.
이 지침은 이 어드민 디렉터리에 적용한다. UI 작업 전에 `design/README.md`와 `design/admin.tokens.json`을 읽는다.
서버 계약은 `src/api/`와 `../Quote_Collection_Spring/`의 해당 컨트롤러·DTO를 확인한다.

---

## 1. 화면 디자인 규칙

### 1-1. 사용 라이브러리

- 기존 Vite + React + TypeScript + SCSS 프로젝트를 유지한다. 새 프로젝트로 다시 생성하지 않는다.
- 라우팅은 react-router-dom, HTTP 요청은 axios, 서버 상태는 @tanstack/react-query를 사용한다.
- UI 라이브러리와 CSS 프레임워크는 사용자 요청 없이 도입하지 않는다.
- 설치된 의존성은 `package.json`을 기준으로 확인한다. 설치되어 있다는 이유만으로 새 상태 관리 방식을 추가하지 않는다.

### 1-2. 폴더 구조

```text
design/
    admin.tokens.json  디자인 값의 단일 원본
    README.md          디자인 설명과 사용 방법
scripts/
    build-tokens.mjs   JSON을 CSS 변수로 변환
src/
    api/               서버 요청과 API 타입, 공통 axios·QueryClient
    components/        주소에 직접 대응하지 않는 공통 화면 조각
    pages/             주소 하나에 대응하는 화면
    layouts/           사이드바와 공통 페이지 배치
    styles/tokens.css  자동 생성된 디자인 토큰 CSS
    utils/             공통 유틸리티
```

- 주소에 직접 대응하면 pages, 공통 화면 조각이면 components에 둔다.
- 컴포넌트와 페이지의 기존 `index.tsx` + SCSS 파일 구성을 따른다.

### 1-3. 상태

- 입력값, 모달 열림 여부 등 화면 내부 상태는 useState에 둔다.
- 현재 검색어, 선택 카테고리, 페이지 번호도 useState로 관리한다. 별도 요청 없이 URL 쿼리스트링 방식으로 변경하지 않는다.
- 서버 데이터는 React Query의 useQuery·useQueries로 관리하고, 변경 요청은 기존 useMutation 방식을 따른다.
- queryKey에는 결과에 영향을 주는 검색 조건, 카테고리, 요청 페이지 등을 포함한다.
- 등록·수정·삭제 후 관련 쿼리를 무효화한다. 기존 화면의 연관 데이터 갱신 범위를 유지한다.

### 1-4. 스타일 규칙

- 크림색 배경, 짙은 녹색 포인트, 조밀한 표 중심 구성을 유지한다. 새 화면은 가장 유사한 기존 화면을 기준으로 만든다.
- 새로 작성하거나 수정하는 UI 스타일의 색상은 `var(--qc-color-*)`를 사용한다.
- 글자 크기는 `var(--qc-font-size-*)`, UI 글꼴은 `var(--qc-font-family-ui)`를 사용한다. 현재 프로젝트에는 예시의 타입 스타일 클래스 체계가 없으므로 이를 있다고 가정하지 않는다.
- 요소 간격은 `var(--qc-space-*)`, 둥글기는 `var(--qc-radius-*)`를 우선 사용한다.
- 필요한 값이 없으면 기존 토큰을 먼저 검토하고, 표현할 수 없을 때 `design/admin.tokens.json`에 추가한다. 다른 값으로 대체해 현재 디자인을 바꾸지 않는다.
- 기존 로그인 일러스트의 고유 색상·서체, 반응형 치수, 비율·clamp·calc 같은 레이아웃 값은 유지한다. 토큰화만을 위해 일괄 치환하지 않는다.
- 토큰 JSON은 Montage 예제처럼 분류한다. 색상 원본은 `color.atomic`, 용도별 참조는 `color.semantic`, 글자는 `typography`, 간격은 `spacing`, 둥글기는 `radius`에 둔다. `usage`와 `cssVar`를 명시하고, 색상 참조는 `ref`로 연결한다. 현재 light 테마만 구현되어 있다.
- `src/styles/tokens.css`는 `npm run tokens`가 만드는 생성물이다. 직접 고치지 않는다.
- 토큰 변경 후 `npm run tokens`를 실행하고 JSON과 생성된 CSS를 함께 반영한다. dev·build 시작 전에도 자동 생성된다. 개발 서버 실행 중 JSON을 수정하면 다시 생성한다.
- Layout, DataTable, SearchBar, TableActions, RowActions, Badge, Pagination, Modal, TextInput 등 기존 공통 컴포넌트를 재사용한다.
- 주요 작업은 brand-primary, 삭제·오류는 danger 계열 토큰을 사용한다.
- 사용자 요청 범위를 넘어 화면 배치, 문구, 인증, 검색, 페이지 이동, 등록·수정·삭제 동작을 변경하지 않는다.

### 1-5. 확인

- 코드나 토큰 변경 후 `npm run tokens:check`, `npm run lint`, `npm run build`를 실행한다. 토큰을 수정했다면 검사 전에 `npm run tokens`를 실행한다.
- 화면 변경은 명령이 통과해도 브라우저에서 확인한다. 관련 페이지의 데스크톱·모바일 화면과 변경한 모달·상태를 확인한다.
- 브라우저나 서버 연결 문제로 확인하지 못한 항목은 완료 보고에 명시한다.
- 문서만 변경했다면 실제 코드·경로·명령과 문서의 일치 여부 및 `git diff --check`를 확인한다.

---

## 2. REST API 통신 규칙

### 2-1. 서버와 인증

- 현재 서버 주소는 `http://localhost:8080`이다. baseURL은 `src/api/axios.ts`의 공통 인스턴스 한 곳에서 관리한다.
- 로컬 프런트엔드는 기본적으로 `http://localhost:5173`을 사용한다. 다른 포트라면 백엔드 CORS 설정을 확인한다. 서버에는 `CORS_ALLOWED_ORIGINS` 설정이 있다.
- 인증 토큰은 기존 sessionStorage와 Bearer 헤더 방식을 유지한다. setAuthToken·clearAuthToken을 사용해 토큰과 React Query 캐시를 함께 처리한다.
- 현재 로그인 요청은 `src/pages/Login/index.tsx`에서 공통 api 인스턴스로 처리한다. 디자인 작업 중 인증 구조를 임의로 바꾸지 않는다.

### 2-2. 데이터 요청

- 새 서버 요청은 `src/api/`의 `.ts` 파일에 정의한다. 화면에서는 해당 API 함수를 호출하며 별도의 axios 인스턴스나 fetch 요청을 추가하지 않는다.
- 기존 명언·인물·카테고리 요청은 quoteApi·personApi·categoryApi와 옵션 조회 API를 재사용한다.
- 조회에 React Query가 제공하는 AbortSignal을 전달하는 기존 방식을 유지한다.
- 화면 페이지 번호는 1부터 센다. 명언 목록의 PAGE_SIZE는 `src/api/quote.ts`를 기준으로 한다.
- 명언은 `/quote`, 인물은 `/person`, 카테고리는 `/country`, `/period`, `/field`, `/theme`를 사용한다. 수정·삭제는 기존 id 기반 경로를 따른다.

### 2-3. 응답 처리

- 현재 일반 목록 응답은 `{ result: [...], total, totalPage }` 형태다. 실제 엔드포인트의 타입과 서버 구현을 먼저 확인한다.
- 예시 메뉴 서비스의 `{ httpStatus, message, result: { menus } }` 응답 형식을 이 프로젝트에 적용하지 않는다.
- API 계층에서 응답을 해석한다. quoteApi.list는 `{ items, total, totalPages }`로 변환하고, personApi.list·categoryApi.list는 기존 목록 형태를 반환한다.
- 오류는 기존 isAxiosError 처리와 HTTP 상태 코드를 기준으로 한다. 401·403·네트워크 오류를 구분하고, 서버 메시지는 실제 응답 타입을 확인해 사용한다.
- 오류·빈 목록·로딩·저장 중 상태를 기존 UI 방식으로 표시한다.

### 2-4. 검색과 페이지 처리의 차이

- 명언 일반 목록은 서버 페이지를 사용한다. 명언 검색은 검색 결과를 받은 뒤 화면에서 PAGE_SIZE 단위로 나눈다.
- 인물 검색은 전체 인물 조회 결과를 클라이언트에서 검색·페이지 처리하는 현재 동작을 유지한다.
- 카테고리 검색은 현재 페이지의 목록을 필터링한다. 전체 데이터 검색으로 임의 해석하지 않는다.
- 새 기능의 계약이 불명확하면 관련 API 함수와 백엔드 구현을 확인한다. 예시 프로젝트의 필드나 응답을 추정해서 추가하지 않는다.
