package spring.security.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Permission extends AbstractEntity{
    private String name;
    private String description;
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();
}
