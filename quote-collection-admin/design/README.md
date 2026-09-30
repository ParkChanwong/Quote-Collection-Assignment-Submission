# 어드민 디자인 기준

현재 화면의 크림색 배경과 짙은 녹색 포인트, 조밀한 표 중심 구성을 유지합니다.
Montage 예제의 분류 구조를 참고하고, 값은 기존 어드민 디자인에서 추출했습니다. 현재 light 테마만 정의합니다.

## 파일의 역할

- `AGENTS.md`: Codex가 이 어드민에서 따를 구현·디자인 지침
- `design/admin.tokens.json`: 디자인 값의 단일 원본
- `scripts/build-tokens.mjs`: JSON을 CSS 변수로 변환하는 스크립트
- `src/styles/tokens.css`: 생성된 CSS. 직접 수정하지 않습니다.
- `src/index.css`: 생성된 CSS를 앱에 불러오는 진입점

## 변경 방법

1. `design/admin.tokens.json`의 해당 분류에서 값을 수정하거나 새 토큰을 추가합니다. 색상은 `color.atomic`의 원색을 수정하고 `color.semantic`에서 용도별로 참조합니다.
2. `npm run tokens`를 실행합니다.
3. 항목의 `cssVar`에 적힌 이름으로 SCSS에서 `var(--qc-...)`를 사용합니다.
4. JSON과 생성된 CSS를 함께 커밋합니다. `npm run tokens:check`로 동기화 여부를 확인합니다.

`npm run dev`와 `npm run build`는 시작 전에 CSS를 자동 생성합니다.
개발 서버 실행 중 JSON을 수정한 경우에는 `npm run tokens`를 다시 실행해야 합니다.

## JSON 구조

- `color.atomic`: 원색 값
- `color.semantic`: 용도별 설명과 light 테마 원색 참조 (`ref`)
- `typography.fontFamily` / `typography.fontSize`: 글꼴과 글자 크기
- `spacing`: 간격
- `radius`: 둥글기 (우리 어드민에 맞게 추가)

`value`는 실제 값, `usage`는 용도, `cssVar`는 생성할 CSS 변수명입니다.
`ref`는 이 JSON 내 경로를 참조합니다. 참조 색상에 값을 중복 저장하지 않습니다.
Montage 전체 스키마의 복제본은 아니며, 우리 앱이 사용하는 63개 공통 토큰에 맞춘 구조입니다.
기존에 없는 다크 테마나 글자 스타일 조합은 임의로 만들지 않습니다.

## CSS 토큰 이름

- 색상: `color-brand-primary`, `color-surface-page`, `color-text-primary` 등 용도별 이름
- 글꼴: `font-family-ui`
- 글자 크기: `font-size-숫자` (기존 px 값)
- 둥글기: `radius-숫자`
- 간격: `space-숫자` (gap, padding, margin에 사용 가능)

예: `color-brand-primary`는 `--qc-color-brand-primary` CSS 변수가 됩니다.
로그인 일러스트의 세부 색상, 레이아웃 치수와 반응형 분기점은 기존 SCSS를 유지합니다.

## 화면 추가·수정 원칙

기존 공통 컴포넌트를 재사용하고, 주요 작업에는 짙은 녹색, 삭제·오류에는 기존 danger 색상을 사용합니다.
기존 글자 크기, 표 밀도와 여백을 기준으로 만듭니다. 사용자가 요청하지 않은 화면 배치·기능 변경은 하지 않습니다.
새 토큰은 기존 토큰으로 표현할 수 없을 때만 추가합니다.

## AI 요청 예시

> AGENTS.md의 지침을 따라 현재 디자인을 유지하면서 새 화면을 만들어줘. design/admin.tokens.json의 토큰과 기존 공통 컴포넌트를 사용해줘.

## 확인

`npm run tokens:check`, `npm run lint`, `npm run build`를 실행합니다.
실제 화면을 바꾸는 작업에서는 관련 페이지, 모달과 모바일 화면도 확인합니다.
