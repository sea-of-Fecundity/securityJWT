# 프로젝트 소개
스프링 시큐리티와 jwt access, refresh 토큰을 공부하면서 만든 프로젝트입니다.

## 목차
1. [공부 기간](#공부-기간)
2. [공부하면서 발생한 에러 상황](#공부하면서-발생한-에러-상황)
3. [해결 방법](#해결-방법)
4. [추가적인 기능](#추가적인-기능)
5. [학습에 도움이 된 자료](#학습에-도움이-된-자료)

---

# 공부 기간
+ 24.02.15 ~ 24.04.01 (추가적인 학습으로 프로젝트의 내용이 추가될 수 있다.)

# 공부하면서 발생한 에러 상황
1. /login, /logout 두 개의  url에 접속을 하면 404 error가 발생했다.
2. loginFilter를 addFilterAt 하기 전에 .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);를 하면 에러가 발생했다.
3. before가 아닌 addFilterAfter(new JWTFilter(jwtUtil), LoginFilter.class)를 사용해서 에러가 발생했다. 

# 해결 방법
1. 기존 프로젝트에서 잘 작동 되었는데 6.0 버전에서만 에러가 발생하므로 6.0 버전의 공식문서를 확인했다. 기존의 메소드가 Deprecated되고 새로나온 securityMatcher를 확인하고 기능을 알아보고 사용했다.

[SecurityConfig](src/main/java/com/example/securityjwt/config/security/SecurityConfig.java)

변경전
```java

http.
                .authorizeHttpRequests((auth) -> auth
                    .requestMatchers("/join", "/").permitAll()
                    .requestMatchers("/visitor").hasRole("VISITOR")
                    .requestMatchers("/myPage").hasAnyRole("ADMIN", "USER")
                    .requestMatchers("/test").hasRole("TEST")
                    .requestMatchers("/admin").hasRole("ADMIN")
                    .requestMatchers("/reissue").permitAll()
                .anyRequest().fullyAuthenticated());


```
변경 후

~~~java
http.
securityMatcher( "/**")
                .authorizeHttpRequests((auth) -> auth
                    .requestMatchers("/join", "/").permitAll()
                    .requestMatchers("/visitor").hasRole("VISITOR")
                    .requestMatchers("/myPage").hasAnyRole("ADMIN", "USER")
                    .requestMatchers("/test").hasRole("TEST")
                    .requestMatchers("/admin").hasRole("ADMIN")
                    .requestMatchers("/reissue").permitAll()
                .anyRequest().fullyAuthenticated());

~~~




2. 순서를 변경했다. LoginFilter.class가 만들어 지지 않았는데 addFilterBefore를 해서 발생했다.


# 추가적인 기능
1. 랜덤한 비밀키를 생성하도록 변경 했다.

변경전

     public JwtUtil(@Value("${spring.jwt.secret}") String secretKey) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }
변경 후
    
    public class SecureKeyGenerator {
        public static SecretKey generateKey() throws NoSuchAlgorithmException {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            keyGenerator.init(256); // 키의 길이를 256비트로 설정
            return keyGenerator.generateKey();
        }
    }
    
이유) 비밀키를 파일 어딘가에 저장하는 방식보다는 시스템에서 랜덤한 값을 생성하는 방식이 보안적으로 좋아서 선택했다.

2. spring schedul로 자정에 refresh 토큰을 삭제하기
        
        @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
        public void reportCurrentTime() {
            refreshTokenService.deleteExpiredRefreshToken();
        }
        
        public void deleteExpiredRefreshToken() {
          refreshRepository.findAll().stream()
                  .filter((refreshToken) -> refreshToken.getExpired() <= System.currentTimeMillis())
                  .forEach((domain) -> refreshRepository.deleteById(domain.getId()));
      }


이유) 작은 규모의 프로젝트이므로 spring scheduling 기능으로 충분하다고 생각했다. 



# 학습에 도움이 된 자료
<https://www.youtube.com/watch?v=SxfweG-F6JM&list=PLJkjrxxiBSFATow4HY2qr5wLvXM6Rg-BM>

<https://www.youtube.com/watch?v=NPRh2v7PTZg&list=PLJkjrxxiBSFCcOjy0AAVGNtIa08VLk1EJ>
