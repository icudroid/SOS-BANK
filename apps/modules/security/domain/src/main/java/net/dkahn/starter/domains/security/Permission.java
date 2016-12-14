package net.dkahn.starter.domains.security;

import net.dkahn.starter.tools.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name","status"})
})
@EqualsAndHashCode(of = {"name","right"},callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true, exclude = {"roles"})
public class Permission extends BaseEntity {

    @Id
    @SequenceGenerator(name = "permission",sequenceName = "permission_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "permission")
    private Integer id;

    @Column(length = 32)
    private String name;

    @Column(length = 15,name="status")
    private String right;

    @Column(length = 50)
    private String description;

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles;

}
