package hana.simple.userservice.global

import java.time.Duration

/**
 * 전역변수 관리
 */
val USER_CACHE_TTL: Duration =  Duration.ofDays(2) // 2일 후 캐시삭제
val TOKEN_CACHE_TTL: Duration =  Duration.ofDays(1) // 1일 후 캐시삭제
