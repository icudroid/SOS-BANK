package net.dkahn.starter.domains.security;


import net.dkahn.starter.tools.domain.BaseEntity;
import net.dkahn.starter.tools.domain.converter.LocalDatePersistenceConverter;
import net.dkahn.starter.tools.domain.converter.LocalDateTimePersistenceConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "app_user")
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name="type",
        discriminatorType=DiscriminatorType.STRING
)
public abstract class User extends BaseEntity {

    @Id
    @SequenceGenerator(name = "user",sequenceName = "user_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user")
    protected Long id;

    @Column(unique = true,nullable = false,length = 32)
    protected String login;

    @Column(name="passwd",nullable = false,length = 80)
    protected String password;

    @Column
    protected Boolean enabled;

    @Column
    protected Boolean blocked;

    @Convert(converter = LocalDateTimePersistenceConverter.class)
    protected LocalDateTime expirationDate;

    @Convert(converter = LocalDatePersistenceConverter.class)
    protected LocalDate birthdate;

    @ManyToMany
    @JoinTable( name = "user_profile",
                joinColumns = {
                    @JoinColumn(name = "user_id", nullable = false, updatable = false)
                },
                inverseJoinColumns = {
                    @JoinColumn(name = "profile_id",nullable = false, updatable = false)
                })
    List<Profile> profiles;


}
