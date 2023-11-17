![header](https://capsule-render.vercel.app/api?type=cylinder&color=0:EAEDFE,100:D6FDE9&height=230&section=header&text=matching%20manager&fontColor=000000&fontSize=70&animation=fadeIn&fontAlignY=50&desc=MVVM%20Architecture&descAlignY=70)

# Project 11조(LINK_UP) - MATCHING-MANAGER

⚽️ **프로젝트 소개**

```

"Matching-Manager" 는 스포츠 경기 매칭, 경기장 추천, 용병 모집을 도와주는 스포츠 매칭 플랫폼입니다.

```

</br>

💡 **이런 사람들에게 추천합니다**

```

1. 경기장 추천 : 경기장을 예약할때 근처에 있는 경기장 정보를 알고싶은 사람!

- 지역별, 종목별 필터를 통해 경기장 정보를 제공해 줍니다.

2. 용병 모집 : 경기는 하고싶지만 함께 할 인원이 부족한 사람들!

- 인원이 부족한 사람들은 용병 모집/신청 이 가능해 팀에 가입하거나 인원을 보충 할수 있습니다.

3. 경기 매칭 - 운동을 하고싶지만, 함께 경기 할 상대팀이 없을때!

- 지역, 운동 종목을 나눠 함께 경기를 진행할 상대와 매칭을 도와줍니다.

```

</br>

## ✈️ **기술 스택**
<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=Kotlin&logoColor=white"/> <img src="https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=Android&logoColor=white"/>
<img src="https://img.shields.io/badge/AndroidStudio-3DDC84?style=flat-square&logo=AndroidStudio&logoColor=white"/>
<img src="https://img.shields.io/badge/git-F05032?style=flat-square&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=flat-square&logo=github&logoColor=white">

</br>

---

# 🎲 MVVM - ARCHITECTURE

<img width="365" alt="Architecture" src="https://github.com/matching-manager/matching-manager/assets/106515742/02b53cac-d8ec-4757-835d-bb14ca6367a1">

---

# ⚙️ *Main Stacks*

**Architecture** - DI, MVVM

**Jetpack** - ViewModel, LiveData, Reposittory, LifeCycle, ViewBinding, DataBinding, AAC

**비동기 처리** - Coroutine

**데이터 처리** - Json Deserializer, SharedPreferences

**Firebase** - Realtime DB, Storage, Authentication, Cloud Messaging

**API 통신** - Retrofit, Kakao Search API

**이미지 로더** - Coil

**UI Frameworks** - Fragment, RecyclerViewAdapter, ListAdapter, XML, BottomsheetFragment, MaterialDesign

---

# **🏟️ *Features***

### **1) Sign In + Sign Up**

![sign](https://github.com/matching-manager/matching-manager/assets/106515742/e4dc350a-edbf-43b5-be9e-96f19b2e6b44)

- **쉽고 빠른 로그인!**
- **Google Signln + FireBase Auth**를 사용한 쉽고 간편한 로그인 기능 제공!
- 앱 내에서 사용할 User, 이름, 번호를 입력해 회원가입 진행

---

### **2) home**

![home](https://github.com/matching-manager/matching-manager/assets/106515742/12d29e37-f76d-4624-a157-20c6f9f7c1d6)


- **추천매치, 공지사항 제공**
- 앱의 메인 페이지로 공지사항 및 추천 경기 매칭을 제공하며상단의 버튼 클릭 시
각각 Arena/Alarm 페이지로 연결

---

### **3) Announcement**

![announcement](https://github.com/matching-manager/matching-manager/assets/106515742/ece0f3f7-c677-4136-8155-3a88ffe33bb0)


- **공지사항**
- 앱 소개 및 안내사항을 보여주는 공지사항 페이지

---

### **4) Arena**

![arena1](https://github.com/matching-manager/matching-manager/assets/106515742/6ea0c30b-75ab-4f0e-820f-091088efe89c)


- **경기장 검색 & 공유 & 전화 기능**
- KaKao 장소검색 API를 사용하여 종목별 경기장 정보를 지역별로 보여주고 Url공유, 전화기능을 제공합니다.

---

### **5) Match**

![match](https://github.com/matching-manager/matching-manager/assets/106515742/b10eb46a-a1e1-462d-9010-8bc3a02c10bb)


- **쉽고 빠른 경기 매칭**
- 함께 경기할 상태팀을 구하기 위한 경기매칭 페이지 종목별, 지역별 필터 사용 가능

---

### **6) Team**

![team](https://github.com/matching-manager/matching-manager/assets/106515742/dcb35de0-de38-4f89-88d6-142ce6a30c05)


- **용병 모집/신청**
- 인원이 부족한 사람들을 위한 용병 모집/신청 기능 제공 모집/신청, 지역별 필터 사용 가능

---

### **7) Detail/Add (MATCH/TEAM)**

![detail](https://github.com/matching-manager/matching-manager/assets/106515742/a00528ae-cae7-4586-92af-631b01da2b79)


- 게시글의 **상세정보**를 확인할 수 있는 디테일 페이지
- 게시글을 추가할 수 있는 Add Page 구현
- **관심 목록** 기능 & 매치/**팀 신청기능** 제공

---

### **8) Calendar**

![calendar](https://github.com/matching-manager/matching-manager/assets/106515742/81efd547-a361-4571-a48c-a3af38148da9)


- **간편한 일정 관리**
- **경기 날짜**, **장소**, 가벼운 **메모** 등 사용자가 손쉽게 이용할 수 있는 **일정관리** 기능 제공

---

### **9) My**

![my](https://github.com/matching-manager/matching-manager/assets/106515742/d1d1a8f2-fb6e-40ab-9ea9-34a946113836)


- **USER PROFILE PAGE**
- USER의 프로필 정보를 확인할 수 있는 페이지
- **작성글**/**관심목록을** 한 눈에 확인할 수 있고버튼을 눌러 수정/삭제 가능
- 추가적으로 로그아웃 기능을 제공

---

### - **SEND MSG**

![msg](https://github.com/matching-manager/matching-manager/assets/106515742/3e6b7d25-ad29-4564-ae77-6dab498d3f4f)


- **Send Cloud Messaging** 을 사용
- 각각 디테일 페이지에서
용병/경기 매칭 신청을 지원하는 페이지신청 후 CloudMessage를 통해 데이터 전송

---

### - **ALARM**

![alam](https://github.com/matching-manager/matching-manager/assets/106515742/767bbd54-175e-4dde-a7e9-712c9bfa6d05)


- 빠르고 정확한 알림
- 용병 모집/신청, 경기 매칭 등 알림 서비스 제공 우측상단 Call 버튼으로 전화기능 제공

---

# 👩🏻‍💻🧑🏻‍💻 ***Team members***

<table>
  <tbody>
    <tr>
      <td align="center"><a href="https://github.com/SoftyChoo"><img src="https://avatars.githubusercontent.com/u/132810978?v=4" width="100px;"><br /><sub><b>추민수</b></sub></a><br /></a></td>
      <td align="center"><a href="https://github.com/AgileCatch"><img src="https://github.com/Android-Team-13-Maniacs/android_project_maniacs/assets/106515742/cbfc4f47-03c1-4983-8dfd-b1be424baab6" width="100px;"><br /><sub><b>김영현</b></sub></a><br /></a></td>
      <td align="center"><a href="https://github.com/hjdevelop"><img src="https://avatars.githubusercontent.com/u/126463915?v=4" width="100px;"><br /><sub><b>손현준</b></sub></a><br /></a></td>
      <td align="center"><a href="https://github.com/mwkimm"><img src="https://avatars.githubusercontent.com/u/94061061?v=4" width="100px;"><br /><sub><b>김민우</b></sub></a><br /></a></td>
     <tr/>
  </tbody>
</table>

