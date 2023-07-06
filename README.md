# ColorfulCard App   
*대구시 결식아동을 위한 컬러풀 카드앱 개발*

## 🖥️ 프로젝트 소개
- **컬러풀카드?** <br>대구시에서 결식아동이 지정된 가맹점에서 급식 및 교육 지원을 받을 수 있는 복지카드 

- **개발 동기 및 목적**
<br>설문조사, 인터뷰, 보도 및 문헌 자료조사, 기존 컬러풀카드 웹사이트 분석 진행 
<br>>> 문제점: 가맹점 확인의 어려움, 잔액 확인의 번거로움, 다자녀 카드등록 어려움 등
<br>이런 불편함 해소를 위해 대구시 결식아동 맞춤형 ‘대구시 컬러풀카드 앱’을 기획 및 제작 
<br><u>컬러풀 카드를 소지한 결식아동들의 인권을 보장하고 대구시 결식아동들이 나라에서 지원해주는 복지를 불편함 없이 이용하게 만드는 것</u>
<br>

## 🕰️ 개발 기간
* 2021.08.27 - 2021.12.02

### ⚙️ 개발 환경
- Language : java
- IDE : Android Studio, Eclipse
- DB IDE : Dbeaver
- Framework : Springboot, MyBatis(SQL Mapper)
- Database : MySQL
- Server : AWS Elastic Beanstalk , AWS RDS 

### 🧑‍🤝‍🧑 맴버 구성
 - 팀장  : 박지연 - 로그인/회원가입, 카드등록, 카드조회, 가명점표시, 소통게시판, 카드정보 제공, UI 제작, REST API 구현, DB설계/구현
 - 팀원1 : 오은아 - 로그인, 카드조회, UI 제작, REST API, 대회장 발표   
 - 팀원2 : 이경주 - 로고 제작, UI 제작, 가맹점 상세 카드뷰, 데이터 전처리
 - 팀원3 : 나유경 - 아이디어 기획, UI 제작, Google Map API 연결, 데이터 전처리
 - 팀원4 : 전인서 - 아이디어 기획, UI 제작, 데이터 전처리, 문서 검토
<br>

## 📌 주요 기능
#### 로그인 / 회원가입 
- ID찾기, PW찾기
- 이메일 인증 
    - SMTP 네이버 메일 전송
    - JavaMail API 사용
- 회원탈퇴
 
#### 카드등록 
- 공식 카드 유효성 검증 
    - 대구시 컬러풀카드 웹사이트 크롤링
    - jsoup 라이브러리 사용
- 카드 추가/삭제
  
#### 카드조회
- 총 잔액/ 세부 잔액 확인 (웹크롤링)

#### 가맹점 표시
- Google Map API 연동
- 급식/부식/교육 가맹점 분류
- 가맹점 즐겨찾기 등록/취소
- 가맹점명 키워드 검색
- 가맹점 세부 정보 조회
    - 공공데이터 (가맹점명/전화번호/위도/경도) 활용
    - 공공데이터에 없는 가맹점 정보 데이터 구축

#### 소통 게시판 
- 게시판 글 최신순/조회수/공감수 정렬
- 게시글 작성/삭제
- 게시글 내용 키워드 검색
- 게시글 공감/취소
- 댓글 작성/삭제
- 대댓글 작성/삭제

#### 카드 정보 제공 
- 급식/부식/교육카드 기본 정보 제공
<br>

## 🎥 시연 영상

[[YouTube] <span style="font-size:89%">컬러풀카드앱: 결식아동의 급식카드 사용편의성을 개선하다</span>](https://www.youtube.com/watch?v=qqY8MMLA9m0)
<br>
[![썸네일](https://github.com/ColorfulCard/ColorfulCardApp/blob/master/images/Thumbnail.png)](https://www.youtube.com/watch?v=qqY8MMLA9m0)

<br>
<br>


## 🏆 수상 실적
**2022 지역문제해결 프로젝트 희망이음 경진대회 부문 산업부 장관상(금상)**
<br>
[[영남대 학과소식] <span style="font-size:80%">박지연, 오은아, 이경주 학생, ‘2022 지역문제해결 프로젝트’ 금상(산업부장관상) 수상</span>](https://www.yu.ac.kr/cse/community/news.do?mode=view&articleNo=6183021&article.offset=0&articleLimit=10)
<br>
[[NewDaily 기사] <span style="font-size:80%">경북테크노파크, ‘희망이음 프로젝트 포상’ 4년 연속 산업부장관상 수상_ 봄봄팀</span>](https://tk.newdaily.co.kr/site/data/html/2023/03/23/2023032300024.html)
<br>
<br>

### 🔗 멤버 깃허브
 - 박지연 - [github/codingGoover](https://github.com/codingGoover)
 - 오은아 - [github/eunalove](https://github.com/eunalove)
 - 이경주 - [github/rodwnl](https://github.com/rodwnl)
 - 나유경 - [github/yoogaeme](https://github.com/yoogaeme)
 - 전인서 - [github/returnstonesteel](https://github.com/returnstonesteel)

