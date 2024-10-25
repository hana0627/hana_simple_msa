package hana.simple.userservice.api.common.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class) // Entity 영속, 수정 이벤트 감지
@MappedSuperclass // 상속 및 공통필드 생성.
abstract class AuditingFields (
    @CreatedDate
    @Column(updatable = false)
    @JsonIgnore
    var createdAt: LocalDateTime?= null,
    @Column(updatable = false)
    @CreatedBy
    @JsonIgnore
    var createdBy: String?= null,
    @LastModifiedDate
    @JsonIgnore
    var modifiedAt: LocalDateTime?= null,
    @LastModifiedBy
    @JsonIgnore
    var modifiedBy: String?= null,
){
}
