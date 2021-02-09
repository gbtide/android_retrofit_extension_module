# Retrofit 확장 모듈
- 프로젝트마다 Retrofit 을 다루는 패키징 영역이 있는데 이를 모듈화했습니다.
- 라이브러리는 아니고, 프로젝트에 끌어다 쓸 수 있는 용도로 다듬었습니다.

## 지원 기능
- retry 를 지원합니다.
- xml, json 동시 파싱을 지원합니다.
- path 정의 시, resopnse 로 서비스마다 있는 BaseResponseObject 를 상속받지 않은 POJO(주로 BaseResponseObject body 내 result)를 지원합니다.
- api call에 대한 Error 처리를 확장할 수 있습니다.
- 기존 모듈보다 확장된 RX Response(Completable, Single, Maybe, Flowable 등)를 지원합니다.
- Preload 를 지원합니다.

