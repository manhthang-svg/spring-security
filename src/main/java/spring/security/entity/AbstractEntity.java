package spring.security.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Id;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity
        implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @CreatedBy
    @JoinColumn(name = "created_by_id")
    protected Users createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @LastModifiedBy
    @JoinColumn(name = "updated_by_id")
    protected Users updatedBy;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    protected Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    protected Instant updatedAt;

    @Column(nullable = false)
    protected Boolean deleted = false;

    @Version
    protected Long version;
}