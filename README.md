# Retrofit 확장 모듈 
- Retrofit extension 이하 RE라 명칭합니다.
- 프로젝트마다 Retrofit 을 다루는 패키징 영역이 있는데 이를 모듈화했습니다.
- 라이브러리는 아니고, 프로젝트에 끌어다 쓸 수 있는 용도로 다듬었습니다.

## 모듈 특징
### 무엇을 하는 모듈인가
Call, CallAdpater, ResponseConverter를 모듈 내에서 재정의하고 있습니다.

- RECall : Retrofit Call을 주입받는 RECall입니다. Retrofit Call의 execute, enqueue을 직접 다루고 있어서, 이 시점에서 발생한 여러가지 상황(non 200 error, response parsing error, service에서 정의한 response error 등)을 오류 처리 모듈로 넘겨 특정 Exception 생성을 할 수 있습니다. 또한 특정 Exception에 대한 공통 핸들링을 정의할 수도 있습니다.
- RECallAdpater : RECall을 생성합니다. 이 때 서비스에서 정의한 Custom Annotation 을 파싱하여 옵션으로 넣을 수 있습니다. RX 처리를 위해 RECall을 Wrapping 하는 CallObservable도 생성하기 때문에, rx로 API 요청 시 subscribing 중간에 개발자가 직접 개입할 수 있습니다.
- REResponseConverter : Http 클라이언트 모듈에서 던진 Response를 서비스에 맞게 파싱합니다. 완성된 Response를 최종 요청지에 내려주거나, 게이트웨이 에러, 서비스 에러, 파싱 에러 등을 처리하여 오류 처리 모듈로 전송하는 역할을 합니다. 

### 즉, 아래 기능을 지원합니다.
- Custom Annotation 지원 : @Retry, @Preload, @ResponseType 등
- xml, json 동시 파싱 지원
- interface에서 path 정의 시, BaseResponse(서비스마다 code, message, result를 담고 있는 공통 래핑 클래스)를 넣지 않고 바로 POJO 데이터를 넣을 수 있습니다.


- api call에 대한 Error 처리를 확장할 수 있습니다.
- 기존 모듈보다 확장된 RX Response(Completable, Single, Maybe, Flowable 등)를 지원합니다.
- Preload 를 지원합니다.

### Preload 란?
- 파일 로딩과 네트워크 로딩을 동시에 요청한다.
- 파일 Response가 먼저 도착하면 화면에 즉시 Response 내용을 보여준다.
- 네트워크 Response가 먼저 도착하면 즉시 보여주되, 최신데이터를 파일에 저장한다. 그리고 이후 파일 Reponse는 도착하더라도 보여주지 않는다.
- 단, Response가 파일로 먼저 도착하더라도 네트워크로 Error가 떨어질 수 있는 상황이기 때문에, Error 상황 시 적절하게 잘 대응해야한다. (Error View  보다는 SnackBar가 적절하다.)
