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
public class Profile extends BaseEntity implements IProfile{

    @Id
    @SequenceGenerator(name = "profile",sequenceName = "profile_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "profile")
    private Integer id;

    @Column(unique = true,nullable = false,length = 32)
    private String name;

    @Column(length = 50)
    private String description;

    @ManyToMany
    @JoinTable( name = "profile_role",
            joinColumns = {
                    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id",nullable = false, updatable = false)
            })
    private List<Role> roles;

}
