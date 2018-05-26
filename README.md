# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
* request header의 내용을 한줄씩 읽어와서 핸들링 하는 방법에 대해 생각해볼 수 있었다.
* 웹 서버의 형태(url입력시 응답)로 구성이 되어있는데, 소켓방식이다. http통신은 요청이나 응답시 바로 연결이 끊어지는 형태이다. 둘다 소켓을 사용하지만(소켓은 운영체제가 관리하고 애플리케이션은 소켓연결을 요청) 연결이 끊어지냐, 지속되냐의 차이점인 듯 하다.(확실히 정리가 안됨)
* index.html 을 호출하면 html 페이지를 그리면서 필요한 리소스들을 다시 서버에 요청한다. 그런데, 그때마다 새로운 포트로 받아온다. 소켓으로 연결되어 있어서 같은 포트로 통신을 할거라 예상했는데, 아직 이해가 부족한거 같다.

### 요구사항 2 - get 방식으로 회원가입
* 

### 요구사항 3 - post 방식으로 회원가입
* 

### 요구사항 4 - redirect 방식으로 이동
* 

### 요구사항 5 - cookie
* 

### 요구사항 6 - stylesheet 적용
* 

### heroku 서버에 배포 후
* 