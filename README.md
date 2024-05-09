# SpringTwoTokens

## Spring Boot 2.7.18
### Work with Spring Token Viewer
### Spring Web
### Spring OAuth2 Resource Server

## Version 1
### JWT Token
### Httpie
#### Terminal
1. http :8081 - HTTP/1.1 401 ......
2. $env:TOKEN='......' - Set the token getting from Spring Token View <br>
   http :8081/ "Authorization: Bearer $env:TOKEN" - HTTP/1.1 200 ......
#### Run - Console - Check Logs - HTTP GET the token
2024-05-09 10:30:39.266 DEBUG 11844 --- [nio-8081-exec-1] o.s.web.client.RestTemplate              : HTTP GET https://dev-25781967.okta.com/oauth2/default/v1/keys <br>
3. Run 2. again
#### Run - Console - Check Logs
No new logs - Since the token is cached, NOT HTTP GET again

## Version 2
Function is the same as version 1 but defined SecurityFilterChain 
### SecurityFilterChain
### oauth2ResourceServer with OAuth2ResourceServerConfigurer::jwt

## Version 3
### Opaque Token - No Cache, ask from the sever every request
#### Terminal
1. $env:TOKEN='......' - Set the token getting from Spring Token View <br>
   http :8081/ "Authorization: Bearer $env:TOKEN" - HTTP/1.1 200 ......
#### Run - Console - Check Logs - HTTP GET the token
2024-05-09 11:21:52.844 DEBUG 2532 --- [nio-8081-exec-3] o.s.web.client.RestTemplate              : HTTP POST https://dev-25781967.okta.com/oauth2/default/v1/introspect <br>
2. Run 2. again
#### Run - Console - Check Logs
2024-05-09 11:30:14.086 DEBUG 2532 --- [nio-8081-exec-5] o.s.web.client.RestTemplate              : HTTP POST https://dev-25781967.okta.com/oauth2/default/v1/introspect

## Version 4
### Get -> JWT Token; Post -> Opaque Token
### AuthenticationManagerResolver
### RequestMatcherDelegatingAuthenticationManagerResolver
### .oauth2ResourceServer - authenticationManagerResolver
#### Terminal
http :8081/ "Authorization: Bearer $env:TOKEN" <br>
http --form :8081/ "Authorization: Bearer $env:TOKEN" "message=YouTube"


## Reference
### JWT Decode
https://jwt.io/

### Spring Boot Remote vs Local tokens
https://developer.okta.com/blog/2020/08/07/spring-boot-remote-vs-local-tokens