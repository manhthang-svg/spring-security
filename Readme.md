
### REFRESH TOKEN:
+, logic create refreshtoken function: 

- Step 1 : find user to set refreshtoken

- Step 2 : Set time out

- Step 3 : set key for refreshtoken( it can be UUID string )

- Step 4 : save to db
### ROTATE REFRESHTOKEN

```
Client gọi /auth/refresh-token
↓
Server lấy refreshToken từ cookie
↓
Tìm token trong DB
↓
Check token tồn tại + chưa hết hạn
↓
Xóa token cũ
↓
Tạo accessToken mới
↓
Tạo refreshToken mới
↓
Set refreshToken mới vào cookie
↓
Trả accessToken mới về FE
```
### Flow login
- step 1: client gửi username,password 
- step 2 : Authentication Filter nhận request 
- step 3 : Gọi đến authenticationManager và tạo đối tượng UsernamePasswordAuthenticationToken sau đó chuyển cho AuthenticationProvider. Tại đây , authenticationProvider gọi đến DaoAuthenticationProvider sẽ dùng hàm loadByUsername để xác thực người dùng, nếu đúng lưu thông tin vào security context , nếu sai ném ra exception
- step 4 : Load extraClaims gồm role và permission thông qua userdetails 
- step 5 : Tạo access token dựa trên extra claims và user
- step 6 : Tạo refresh token dựa trên user
- step 7 : Gửi raw refresh token vào HttpOnly Secure Cookie



### Note:
- những phần đang thiếu của dự án :
+ , không có rate limiting khi đăng nhập => có thể cải thiện bằng redis đếm số lần ip falese
- note :
  +, we should only put @Mappedby in the inverseside if we need to inverse access
  +, example : we need to know user from refreshtoken but dont need to know refresh token from user
- in a transaction , the insert command always be processed at first, regardless the delete command placed before insert command, at thís point the delete command will be púh to a queue. If you want to force delete command run , you must use repository.flush,ex : refreshRepository.flush directly below the delete command
- delete command always need @Transactional at service, otherwise, spring will warn entitymanager blabla



