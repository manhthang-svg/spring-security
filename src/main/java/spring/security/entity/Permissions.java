package spring.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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
    @ManyToMany(mappedBy = "permissions")
    private Set<Roles> roles = new HashSet<>();
}
