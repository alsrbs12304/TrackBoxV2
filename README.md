# TrackBox

택배 배송 추적과 상태 변경 알림을 제공하는 Android 앱.
기존 [TrackBox](https://github.com/alsrbs12304/TrackBox) 앱(Fragment + XML + Koin + Gson 기반)을 모던 스택으로 새로 작성한 V2 프로젝트입니다.

---

## 주요 기능

- 운송장 등록 (택배사 + 운송장 번호 + 별칭)
- 배송 목록과 마지막 이벤트 한눈에 보기
- 상세 화면에서 진행 타임라인 확인
- 백그라운드에서 30분마다 자동 갱신, 상태가 바뀌면 알림

지원 택배사 (10종)
> CJ대한통운, 우체국택배, 대신택배, 쿠팡 로지스틱스, CU 편의점택배, 천일택배, GS Postbox, CWAY (Woori Express), 딜리박스, LX 판토스

---

## 기술 스택

| 영역 | 선택 |
|---|---|
| 언어 / 컴파일러 | Kotlin 2.0.20 (K2) |
| UI | Jetpack Compose · Material 3 (Dynamic Color, 다크/라이트 자동) |
| 아키텍처 | MVVM + UDF · `StateFlow<UiState>` |
| 레이어 | data / domain / ui (Clean-lite) |
| DI | Hilt (+ Hilt-Work, Hilt-Navigation-Compose) |
| 비동기 | Coroutines + Flow |
| 네트워크 | Retrofit + OkHttp + kotlinx.serialization |
| DB | Room (KSP, `exportSchema = true`) |
| Navigation | Navigation Compose, `@Serializable` 타입 안전 라우트 |
| 백그라운드 | WorkManager (PeriodicWork 30분) |
| 빌드 | Gradle 8.7 · AGP 8.5.2 · Version Catalog (`gradle/libs.versions.toml`) · Kotlin DSL |
| JDK | 17 |
| minSdk / targetSdk | 26 / 34 |

---

## 아키텍처

```
ui (Compose · ViewModel · UiState)
        │ Domain Model
        ▼
domain (model · repository interface)
        │ Domain Model
        ▼
data (api · db · repository impl)
        │
        ├─ Retrofit V1 REST  ← 현재
        └─ (V2 GraphQL)      ← 향후, 인터페이스만 그대로 두고 구현체 교체
```

- **`ui`** 는 `domain` 모델만 본다. DTO/Entity는 일절 노출하지 않음
- **`domain`** 은 순수 Kotlin (Android · Room · Retrofit 의존성 0)
- **`data/repository/TrackingRepositoryImpl`** 이 Room + Retrofit을 결합. `addTracking` 시 DB 시드 → 원격 조회 → 응답 머지

### API 전략

현재는 인증 불필요한 `tracker.delivery` **V1 REST** (`https://apis.tracker.delivery/carriers/{carrierId}/tracks/{trackingNumber}`) 를 사용합니다.
V2 GraphQL은 Console 가입 + API 키 발급이 필요하므로, **Repository 인터페이스 + DataSource 분리** 구조로 두어 키를 받게 되면 `data/api/v2/` 추가하고 Hilt 바인딩만 갈아끼우면 됩니다. UI/Domain은 한 줄도 안 바뀝니다.

---

## 빌드 / 실행

### 필요 환경
- macOS / Linux / Windows
- Android Studio Hedgehog (2023.1) 이상 권장
- JDK 17
- Android SDK Platform 34

---

## 프로젝트 구조

```
app/src/main/java/com/mgpark/trackbox/
├─ TrackBoxApp.kt              @HiltAndroidApp · WorkManager.Configuration.Provider · 알림 채널
├─ MainActivity.kt             @AndroidEntryPoint · SplashScreen · enableEdgeToEdge · NavGraph
│
├─ core/
│  ├─ designsystem/theme/      Color · DeliveryColors · Type · Shape · Theme
│  └─ util/TimeFormat.kt
│
├─ domain/                     순수 Kotlin
│  ├─ model/                   Carrier · TrackingState · Tracking · TrackingProgress · TrackingDetail
│  └─ repository/TrackingRepository.kt
│
├─ data/
│  ├─ api/{TrackerV1Service, v1/{Dto, V1Mapper}}
│  ├─ db/{TrackBoxDatabase, TrackingDao, TrackingEntity, Converters}
│  └─ repository/TrackingRepositoryImpl.kt
│
├─ di/                         NetworkModule · DatabaseModule · RepositoryModule
│
├─ notification/               DeliveryNotifications · DeliveryRefreshWorker · DeliveryRefreshScheduler
│
└─ ui/
   ├─ navigation/              @Serializable 라우트 + NavGraph
   ├─ common/                  StatePill · TrackingStateUi · SamplePreviewData
   ├─ home/                    Screen · ViewModel · UiState · Previews
   ├─ add/                     Screen · ViewModel · UiState · Previews (FieldShell · CarrierBottomSheet · HyphenateTransform)
   └─ detail/                  Screen · ViewModel · UiState · Previews (SummaryHeader · Canvas Timeline)
```

---

## 컨벤션

이 프로젝트에서 사용하지 않는 것:
- XML 레이아웃 · Fragment · ViewBinding · DataBinding → **모든 UI는 Compose**
- kapt → **KSP**
- Gson → **kotlinx.serialization**
- Koin → **Hilt**
- `Call<T>` 콜백 스타일 Retrofit → **`suspend fun` + Coroutines**

화면당 단일 `UiState` data class 로 노출, ViewModel은 `StateFlow<UiState>` 만 외부에 보여줍니다. DTO ↔ Domain 변환은 `data` 레이어에서. UI는 Domain Model 만 봅니다.

---

## 라이선스

개인 프로젝트.
