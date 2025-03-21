**API 명세서**

1. User

![image](https://github.com/user-attachments/assets/95311bd8-f6c9-41bd-b605-0b56c1b271c7)

![image](https://github.com/user-attachments/assets/a55a741b-341a-42e0-b260-9c62c50c75a3)

2. Post

![image](https://github.com/user-attachments/assets/7c8d0e4b-c05a-47c1-af15-4aee5840a46c)

![image](https://github.com/user-attachments/assets/c800ae70-6030-48f0-9293-9e8d25061e16)

3. Auth

![image](https://github.com/user-attachments/assets/5cc0d3ce-e45a-4a28-a5d1-43cf279aab7b)

4. Comment

![image](https://github.com/user-attachments/assets/d5d56e01-e3d0-4508-9ec8-c1745ae60773)

5. Like

![image](https://github.com/user-attachments/assets/f93fa172-e265-48a4-8a8b-a5dfc879cc46)

**ERD**

![image](https://github.com/user-attachments/assets/302fcba1-e893-421e-a1b6-674035111665)

**API 호출**

1. Auth

1.1 회원가입

![image](https://github.com/user-attachments/assets/6d6119bd-5140-4c56-a9a7-96a6a5a9f388)

1.2 로그인

![image](https://github.com/user-attachments/assets/7eadcd72-2c56-4f67-8d30-aa4b48c72255)

2. User
   
2.1 사용자 조회

![image](https://github.com/user-attachments/assets/f248fb40-bc65-4df2-9c1a-d4bd6dd79f8e)

2.2 사용자 정보 수정

![image](https://github.com/user-attachments/assets/997b41b6-b6b2-45fd-aa81-7a518381fd1e)

2.3 비밀번호 변경

![image](https://github.com/user-attachments/assets/ea7185df-3efe-49c3-aaf5-435f1c6bcfc4)

2.4 사용자 삭제

![image](https://github.com/user-attachments/assets/bd99a5cd-61d8-42b1-995c-be534b9e7a97)

![image](https://github.com/user-attachments/assets/a3723bd5-27e0-4547-9c5d-80aeb2432a43)

2.5 사용자 복구

![image](https://github.com/user-attachments/assets/9f2c3f82-f8c2-4c59-8cc3-e6bd15fe3f0f)

![image](https://github.com/user-attachments/assets/753d8d54-e806-4725-8685-633bd69d5154)

2.6 팔로우

![image](https://github.com/user-attachments/assets/90fa18a2-60a4-4c82-920b-af71ed2ef6c6)

2.7 팔로잉 리스트 조회

![image](https://github.com/user-attachments/assets/39de3602-8d81-4dc8-b2da-c44d64702ea7)

2.8 팔로워 리스트 조회 (2.6 에서 팔로우 추가 된 사용자 측면)

![image](https://github.com/user-attachments/assets/fbe625ff-86d8-4f85-b9b1-9f0b32f98922)

2.9 언팔로우

![image](https://github.com/user-attachments/assets/a836200a-24f7-4aab-ab89-2eb09606c022)

3. Post

3.1 게시물 생성

![image](https://github.com/user-attachments/assets/6bf504b8-5da5-4466-a88a-260d9df27675)

3.2 게시물 다건 조회

![image](https://github.com/user-attachments/assets/5512c729-d5e0-4891-aa39-af3a76f7aeb4)

3.2.1 좋아요 순 정렬

![image](https://github.com/user-attachments/assets/8b1e7325-a40a-41f9-b327-74a6fe0797ae)

3.2.2 수정일 순 정렬

![image](https://github.com/user-attachments/assets/efc76c3c-1cdc-45fc-a3a9-dd128d910ccc)

3.3 게시물 기간별 검색

![image](https://github.com/user-attachments/assets/f6d93bc9-671e-46d1-8e0a-3bd8a31da4e8)

3.4 팔로잉한 사용자들의 게시물 다건 조회(사용자1의 팔로잉 리스트 : 사용자2, 사용자3)

![image](https://github.com/user-attachments/assets/fd5a472d-cbdc-49d7-8bae-e9401cd6736f)

3.5 게시물 단건 조회

![image](https://github.com/user-attachments/assets/ea1191dc-54e4-4be4-b951-c70e6279788a)

3.6 게시물 수정

![image](https://github.com/user-attachments/assets/d0eda9fa-ca11-4f76-a6de-b865caf8ce84)

3.7 게시물 삭제

![image](https://github.com/user-attachments/assets/5f3c6a9f-99b3-4fbb-8b4c-797569a63141)

3.8 게시물 복원

![image](https://github.com/user-attachments/assets/7a80edd6-b29d-4149-b931-0d93570caa6f)

4. Comment

4.1 댓글 생성

![image](https://github.com/user-attachments/assets/fe881567-7c4f-4fdb-be6b-c60ff5209f94)

![image](https://github.com/user-attachments/assets/e113a845-93cc-4fa6-8ca4-f1bc4a886542)


4.2 댓글 전체 조회

![image](https://github.com/user-attachments/assets/a4a0d92d-48be-42f1-93a3-51202a3244d3)

4.3 댓글 수정

![image](https://github.com/user-attachments/assets/75604091-5725-471f-869c-481291e66520)

4.4 댓글 삭제

![image](https://github.com/user-attachments/assets/fc10f7a9-65a9-4419-a9b3-33963bebcfe3)

![image](https://github.com/user-attachments/assets/d639eb18-6a31-4b9a-8f23-c8e7fc04a721)

5. Like

5.1 게시물 좋아요

![image](https://github.com/user-attachments/assets/1f725886-d93c-469a-8a9e-59c8ba942398)

![image](https://github.com/user-attachments/assets/6acfbd24-719d-4dfb-9aae-9bf1360b7f54)

5.2 게시물 좋아요 취소

![image](https://github.com/user-attachments/assets/ca71afc3-9073-44c8-8636-a96b8e825725)

![image](https://github.com/user-attachments/assets/f0f922eb-40a5-4a7b-a0b6-b975ad9dab57)

5.3 댓글 좋아요

![image](https://github.com/user-attachments/assets/9a91a863-4ae0-43e3-b5c6-2b94dfa58372)

![image](https://github.com/user-attachments/assets/20f339f1-1692-4e18-aacb-4ec01c455d99)

5.4 댓글 좋아요 취소

![image](https://github.com/user-attachments/assets/a631dbbf-65ac-4cde-8715-760381481efa)

![image](https://github.com/user-attachments/assets/3aaedcd1-40c0-4dce-b6a9-2064145ec012)







