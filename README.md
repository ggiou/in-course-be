# 📝InCourseBE(실전 프로젝트)   
<img src="https://user-images.githubusercontent.com/110077343/198350421-e607f10e-e4a4-49e7-a9dd-d6fc9dad2c09.jpg"></img><br/>  

📌 프로젝트 소개
------------- 
인코스 - 인싸들의 데이트, 친목, 모임 코스.    
친구와 갑자기 약속을 잡았을 때, 데이트 코스를 잘 준비하고 싶을 때, 모임의 장소를 정하는 역할을 맡았을 때, 하루를 알차게 보내고 싶을 때!  
무엇을 먹고 어디를 갈지 누구나 한번쯤 고민해본 경험이 있으실거에요. 
현재 날씨에 맞는 코스와 의상, 아이템까지... 저희 인코스는 이런 고민을 한번에 해결할 수 있는 서비스를 만들고 싶었습니다.  
날씨와 계절, 지역, 성별 맞춤 추천으로 완벽한 하루를 보내고, 나만의 좋은 코스도 다른 사람들과 공유할 수 있는 서비스를 제공합니다.

배포 사이트: https://incourse.me/  
GitHub: https://github.com/InCourseProject/in-course-be

:date: 제작 기간
-------------   
2022.09.16 ~ 2022.10.28 

:family: 팀 멤버 소개 & 담당 기능 구현
-------------   
|이름|포지션|담당 기능 구현|
|------|---|---|
|김동훈|BE|메인페이지, 장소CRUD, 추천 서비스 기능 구현, 서버 배포, GitGub, ERD 관리 등|
|마지우|BE|회원가입, 로그인(JWT, 카카오, 네이버, 이메일 인증), 마이페이지 기능 구현 날씨 API 등|
|김하늘|BE|이미지 업로드(s3), 코스CRUD, 찜하기, 코스 평가점수, 카테고리 조회, 검색 기능 구현|
|김대현|FE|회원가입, 로그인, 코스 찜하기, 코스 평가 점수 기능 구현 등|
|이희수|FE|코스CRUD, 장소CRUD, 지도 API, 마이페이지 조회 기능 구현 등|

:computer: 프로젝트 주요 기능
-------------  
### 1) 메인페이지: 날씨 맞춤 의상과 아이템 추천 & 오늘의 코스 추천   
<img src="https://user-images.githubusercontent.com/110077343/198362950-d1467af3-881a-4d55-a1e6-ee5138171dfd.png"></img><br/>       
### 2) 회원가입 & 로그인: 이메일 인증을 통한 회원가입, 카카오/네이버를 통한 회원가입
<img src="https://user-images.githubusercontent.com/110077343/198349330-6db61e66-4cb4-4eb6-b87e-4e118cc0370b.jpg"></img><br/>     
### 3) 장소(카드)와 코스 작성 페이지: 카드 작성 및 이미지 업로드, 지도 API
<img src="https://user-images.githubusercontent.com/110077343/198349450-6195dc47-0d3a-4ce3-a428-0ba565637a00.jpg"></img><br/>  
### 4) 코스(게시글) 찜하기, 장소(카드) 찜하기, 코스(게시글) 평가 하기, 랭킹 배찌
<img src="https://user-images.githubusercontent.com/110077343/198362817-1d660a39-2931-4a6b-a933-5029fed0446e.png"></img><br/>     
### 5) 검색 기능
<img src="https://user-images.githubusercontent.com/110077343/198350233-c608ffe6-09cb-4586-b150-ce640fd030e7.jpg"></img><br/>      
### 6) 마이페이지: 내가 쓴 글 조회, 내가 찜한 코스 조회   
<img src="https://user-images.githubusercontent.com/110077343/198358995-b5f9043c-714e-4b05-97ba-1fc9fe70a68f.png"></img><br/>  

:movie_camera: 시연 영상
-------------  
/* [![제목](http://img.youtube.com/vi/유튜브썸네일.jpg)](https://www.youtube.com/watch?v=고유번호) */

:green_book: 와이어프레임
------------- 
<img src="https://user-images.githubusercontent.com/110077343/194228885-47af482a-5ab7-4dc7-aa79-4c0b2cdf06ba.png"></img><br/> 
<img src="https://user-images.githubusercontent.com/110077343/194229015-aa30c001-e99c-42e9-8c27-621d6b81d1d0.png"></img><br/>  
<img src="https://user-images.githubusercontent.com/110077343/194229116-226d747d-f463-43ee-87b9-31a6c3497142.png"></img><br/>  
<img src="https://user-images.githubusercontent.com/110077343/194229157-00f698b1-3036-4892-a880-41118112b11e.png"></img><br/>  

:blue_book: ERD
-------------
<img src="https://user-images.githubusercontent.com/110077343/198515914-17525309-4c2d-4425-8c0f-fbba0c9d3890.png"></img><br/>  

:orange_book: API
------------- 
노션: https://www.notion.so/7-3dede89212784077a5097e8873d86bde#5b8632e055b84e0b990ff1d765e71d2f

:ledger: 서비스 아키텍처
-------------
<img src="https://user-images.githubusercontent.com/110077343/198351688-680dc28f-3f0c-4e92-9797-8e84f726088c.png"></img><br/>  


:construction_worker: 기술 스택 & Tools
------------- 
협업 Tools       
<img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white">
<img src="https://img.shields.io/badge/Google Sheets-34A853?style=for-the-badge&logo=Google Sheets&logoColor=white">
<img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white">
<img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white">
<img src="https://img.shields.io/badge/Sourcetree-0052CC?style=for-the-badge&logo=Sourcetree&logoColor=white">     

Backend   
<img src="https://img.shields.io/badge/Java11-007396?style=for-the-badge&logo=Java11&logoColor=white">
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white">
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">
<img src="https://img.shields.io/badge/JSON Web Tokens-000000?style=for-the-badge&logo=JSON Web Tokens&logoColor=white">  
<img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white">
<img src="https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=Ubuntu&logoColor=white">
<img src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white">
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"> 
<img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=for-the-badge&logo=IntelliJ IDEA&logoColor=white"> 
<img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white"> 


Frontend    
<img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=React&logoColor=white">
<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white">
<img src="https://img.shields.io/badge/Axios-5A29E4?style=for-the-badge&logo=Axios&logoColor=white">
<img src="https://img.shields.io/badge/Redux-764ABC?style=for-the-badge&logo=Redux&logoColor=white">
<img src="https://img.shields.io/badge/React Router-CA4245?style=for-the-badge&logo=React Router&logoColor=white">

        
          

:rage: Trouble Shooting
------------- 
BE  
1) 데이터베이스 초기화 전략 설정 문제로 인한 코스 전체 조회, 상세 조회,  검색, 카테고리 조회 등 모든 조회 기능 응답 에러 발생, ddl-auto 옵션: create, create-drop, update, validate, none 중 update는 변경된 스키마만 반영하는 설정을 함, 개발 진행중이거나 테스트 중에 사용하는 옵션임, 현재는 개발 중이므로 update옵션으로 설정하고 발생한 에러는 추가된 컬럼에 NOT NULL 설정이 되어 있고 기존에 저장되어 있는 데이터에 해당 컬럼이 null값이기 때문에 발생한 것이므로 값을 임의로 넣어주어 에러 해결
2) 코스가 삭제되면 장소, 찜하기, 스코어 등도 함께 삭제되도록 영속성 전이를 위해서 1:n(OneToMany)관계에 cascade, orphanRemoval 설정, 양방향 관계로 인하여 fetch문제와 순환참조 문제가 발생, 코스와 장소, 찜하기, 스코어는 모두 코스가 중심이 되는 Entity들이기 때문에 양방향 관계가 되어도 영속성 전이 설정이 필수적으로 필요하다고 판단, 이로인한 양방향관계에서 발생하는 에러인 순환 참조 문제는 @JsonIgnore를 이용하여 해결   
3) 네이버, 카카오 로그인과 같이 api를 통해 소셜 로그인 시 백, 프론트에서 각각 작동하나 연결 후 작동이 되지 않는 오류 발생, 소셜 로그인 시 사용되는 key, id등이 프론트의 값들을 받아와 서로 값을 맞춰야 작동 한다는 것을 인지, 카카오의 경우 key값만 맞춰도 잘 작동하였으나 네이버는 요청 uri에 state가 추가적으로 들어가야 작동한 다는 것을 인지하였고 uri 및 키 값들 요구사항을 다 맞추며 해결    
4) 최근 게시물을 나타내주는 newPost값의 ColumnDefault("true")디폴트를 true로 설정하고 스케줄러로 일주일이 지나면 false로 바꾸어, 최근 게시물을 알아볼수 있게 기능 개발하였고 게시물을 생성할 때 디폴트값인 true가 아닌 false로 생성됨 @ColumnDefault("true")은 값이 null일때 true로 바꿔주는 기능이었음 newPost값 자체를 바꿔서 해결   
5) 협업 시 코드 배포를 매번 코드를 배포해주어야 하는데 수동 배포에 너무 많은 시간이 필요한 상황, AWS CodeDeploy는 IAM Role과 같은 설정이 추가적으로 필요하지만, 현 프로젝트에서 AWS ec2서버를 활용하고, 여러 서버 배포 시 용이하여 관리의 편의성과 보안의 리스크를 줄이고자 Github Actions + AWS CodeDeploy를 활용하여 배포

   
FE  
1) 게시물 작성 카드 추가시 새로고침으로 인한 데이터 사라짐 현상 (이미지) localstorge 에 저장 했지만 localstorge는 string만 저장 가능 하다는 것을 인지하였고, 카드 작성 버튼 클릭시 localStorage 에 filebase64 형태로 저장, useEffect때 getItem 으로 불러온 뒤 setState에 다시 넣어주고 데이터를 보낼 때 blob으로 file형태 변환 후 폼데이터로 묶어서 보내주어 해결 
2) 상세게시물에 map 함수를 사용 할 때 map 함수를 읽을 수 없다 라는 오류가 생김, get 요청을 useEffect시 실행 하는데 렌더링이 되기 전에 map함수가 먼저 작동되어 배열이 undefined 상태라 오류가 생기는것을 인지하였고 처음에 ‘?’로 예외처리를 해주었지만 initialState 기본값을 넣어주어 해결      
