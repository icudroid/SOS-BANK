package net.dkahn.starter.domains.security;


import net.dkahn.starter.tools.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table
@EqualsAndHashCode(of = "name",callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true, exclude = {"permissions"})
public class Role extends BaseEntity {

    @Id
    @SequenceGenerator(name = "role",sequenceName = "role_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "role")
    private Integer id;

    @Column(length = 32, unique = true, nullable = false)
    private String name;

    @Column(length = 50)
    private String description;

    @ManyToMany
    @JoinTable( name = "role_permission",
            joinColumns = {
                    @JoinColumn(name = "role_id", nullable = false, updatable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "permission_id",nullable = false, updatable = false)
            })
    private List<Permission> permissions;

}
