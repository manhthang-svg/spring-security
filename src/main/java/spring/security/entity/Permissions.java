package spring.security.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "permissions")
public class Permissions extends AbstractEntity{
    @Column(nullable = false,unique = true)
    private String name;
    private String description;
    @ManyToMany(mappedBy = "permissions",fetch = FetchType.LAZY)
    private Set<Roles> roles = new HashSet<>();
}
