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
=>> 자고일어나서 다시 소스를 확인하니 thread 하나에서 response 한번주고 나서 thread가 종료되어서 그렇다...

### 요구사항 2 - get 방식으로 회원가입
* url에서 회원가입에 대한 path를 포함하면 parseQueryString 메서드를 이용하여 파라미터를 map으로 담고 다시 user 클래스로 저장하도록 만들었다.
* 사실 lambda에 대해 모른다. 기본서 볼 때 그냥 지나쳤던 부분인데, 해당 메서드를 보며 lambda에 대해 조만간 다시 학습하는 시간을 가져야겠다고 생각했다.
* commit 과정에서 충돌이 생겨서 엄청나게 삽질.. 이클립스에서 다른 위치로 git 설정을 해두고는 터미널에서 예전에 했던 프로젝트로 commit을 해서 충돌발생.. 충돌나는 파일 지우고 merge 완료

### 요구사항 3 - post 방식으로 회원가입
* 예전에 했던 프로젝트와 충돌나서 프로젝트 구조가 조금 꼬인듯 함. => 일단 프로젝트 진행에 무리가 없어 무시하고 진행
* BufferedReader 의 데이터에서 Content-Length를 포함(contain)하는 라인을 찾아 본문 길이를 측정
* 힌트를 안보고 했다가 IOUtils를 사용안하고 reader를 조작하는 방법으로 하다가 실패.. 첫번째 read시 공백이 나오면 read를 멈추기 때문에 다시 읽으면 다음 부분부터 나오겠지하고 생각함. => 일부는 맞은거 같으나 .. 다시 같은 방법으로 읽고 나서 null값이 올때 while문을 멈추었으나 안멈추고 null을 무한히 뱉어냄

### 요구사항 4 - redirect 방식으로 이동
* 위키피디아에서 302 코드에 대해 찾아봄 -> url redirection해주고 Server에서 header에 HTTP/1.1 302 Found와 함께 Location필드에 url을 입력하여 준다.

### 요구사항 5 - cookie
* 미숙한 null 체크와 디버깅으로 시간 낭비 => test case 작성법 공부해야함.. 안되면 디버깅부터..
* 방법 자체는 간단하게 해결되었다. response 해주던 메소드에 override 해서 cookie 추가
* 실패시 redircetion 필요해서 302 status 사용

### 요구사항 6 - 사용자 목록 출력
* StringBuilder 사용방법을 구글링해서 append로 tag와 사용자 목록을 넣고 다시 검색을 통해 StringBuilder를 byte array로 convert 해서 response body에 넣어주었다.
* DataBase 에서 Collection으로 User클래스를 받아와서 Iterator 사용하여 동적으로 사용자 목록을 출려하였다.
* html의 구조나 디자인적인 측면은 따로 고려하지 않았다...

### 요구사항 7 - stylesheet 적용
* 응답헤더의 Content-Type으로 content가 무엇인지를 판별한다는 것을 확인하였다.

### heroku 서버에 배포 후
* 