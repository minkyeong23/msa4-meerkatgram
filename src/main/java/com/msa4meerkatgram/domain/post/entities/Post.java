package com.msa4meerkatgram.domain.post.entities;

import com.msa4meerkatgram.domain.user.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity // 해당 클래스가 JPA 엔티티임을 선언
@EntityListeners(AuditingEntityListener.class) // 엔티티의 이벤트 리스너 지정, 개별 엔티티 클래스 위에 붙어서 "이 엔티티가 저장되거나 수정될 때 시간을 감시하겠다"라고 알려주는 역할
@Table(name = "posts") // 테이블명 맵핑
@SQLDelete(sql = "UPDATE posts SET deleted_at = NOW() where id = ?") // Soft delete 활성화
@SQLRestriction("deleted_at IS NULL") // 엔티티의 조회 시 항상 특정 조건을 추가하도록 지정
@Getter
@Setter
public class Post {
    @Id // PK 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 자동 생성 전략 (strategy : AUTO, IDENTITY, SEQUENCE, TABLE)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // "현재 엔티티(Many)가 User 엔티티(One)를 참조한다"는 N:1 관계를 선언
    @JoinColumn(name = "user_id", insertable = true, updatable = false, nullable = false)
    private User user;

    @Column(name = "content", nullable = false, length = 200)
    private String content;

    @Column(name = "image", nullable = false, length = 100)
    private String image;

    @CreatedDate // 생성 시 자동으로 시간 입력
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // 수정 시 자동으로 시간 업데이트
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;
}