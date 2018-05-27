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
* http header의 구조를 파악하도록 유도하고 header의 정보를 이용하는 방법에 대해 생각해볼 수 있는 부분이었다.
* 우선 hint 1의 내용처럼 BuffredReader를 이용하여 InputStream을 한줄씩 읽어왔다.(readLine메소드)    
   검색키워드 - java inputstream bufferedreader  
   참고글 - https://stackoverflow.com/questions/5200187/convert-inputstream-to-bufferedreader
* 일단은 한줄씩 읽어온 header의 모든 부분을 출력해보았는 데, 첫째줄에 사용자가 지정하는 경로가 포함되어 있는 것을 볼 수 있었다.
  첫째줄의 두번째 인자로 경로가 들어온다. String class의 split 메소드를 이용하여 (split 메소드의 파라미터는 정규표현식) 공백을 
  기준으로 잘라서 두번째 인자를 추출하는 메소드를 작성하였다.
* 파일데이터를 byte 배열로 읽어들이는 Files.readAllBytes 메소드의 사용방법에 대해서도 알게되었다. => Files.readAllBytes(Path)   

### 요구사항 2 - get 방식으로 회원가입
* get방식으로 요청을 보내면 쿼리스트링으로 url의 뒷부분에 요청데이터가 추가된다. url과 "?" 문자로 구분되며, key=value 의 형태로 추가된다.
  값이 여러개일 경우 key=value 사이에 "&"로 묶어서 나타난다.
* 요청 path와 parameter를 분리해보는 메소드를 jUnit으로 test 해보았고, util.HttpRequestUtils 클래스의 parseQueryString()메소드의 
 흐름을 살펴보며 어떻게 사용하는 지를 확인하였다.
* parameter를 Map<String,String> 에서 가져와 User 클래스에 저장(인스턴스를 생성) 하였다. 따로 메소드를 만들어 Map의 value값으로 User클래스의 인스턴스를 생성했다.

### 요구사항 3 - post 방식으로 회원가입
* post방식으로 요청을 보내면 http body에 데이터가 추가되고 url 부분에서는 보이지 않는다.
* header에서 content-length 부분의 값을 추출해 /user/create 요청이 올 때 길이만큼 데이터를 읽도록 하였다.

### 요구사항 4 - redirect 방식으로 이동
* 302 status code는 url을 redirection 하는 코드이다.
* redirection을 위해 response header에 200 ok 대신 302 Found를 추가하는 코드를 작성하였다.
* 크롬 개발자 도구를 통해 reponse header를 확인한 결과 정상적으로 302 Found를 상태코드로 응답하며 redirection 해주는 것을 확인하였다.
* 완성한것으로 착각하였으나 위키피디아의 문서 내용중 내가 필요한 부분만 수정하면 된다고 생각하여 302 Found 코드부분만 추가하였는 데, 추가 요구사항을 다 읽지 못하여서
생긴 오류로 브라우저의 url이 /user/create로 남아있는 문제가 발생하였다. 리다이렉션 해줄 때 reponse header에 Location 필드를 추가하고 리다이렉션하는 주소를 
value로 주면 브라우저의 url이 해당 주소로 변경이 된다.

### 요구사항 5 - cookie
*

### 요구사항 6 - stylesheet 적용
* 

### heroku 서버에 배포 후
* 