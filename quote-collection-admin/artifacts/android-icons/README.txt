Android 앱 아이콘

res 폴더 내용을 앱의 app/src/main/res에 합치세요.
같은 폴더에 기존 ic_launcher.webp / ic_launcher_round.webp 등 동명 리소스가 있으면 백업 후 교체하여 중복을 없애세요.
AndroidManifest.xml의 application 속성:
android:icon="@mipmap/ic_launcher"
android:roundIcon="@mipmap/ic_launcher_round"

일반 런처 PNG: mdpi=48x48, hdpi=72x72, xhdpi=96x96, xxhdpi=144x144, xxxhdpi=192x192
by-pixel 폴더에도 실제 크기로 이름 붙인 PNG를 넣었습니다.
적응형 전경: 108dp 기준 108/162/216/324/432px. XML 배경은 #284d40입니다.
Android 13 테마용 monochrome 포함: compileSdk 33 이상 사용.
Play 스토어 등록: play-store-512x512.png (512x512, 32비트 RGBA, 배경 불투명).
스토어용 512px 파일은 런처 파일 대신 쓰지 않습니다.

파일 픽셀 크기와 PNG 형식을 검사했습니다. 실제 Android 앱 빌드/기기 적용은 이 프로젝트에서 실행하지 않았습니다.
