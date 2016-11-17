package net.dkahn.starter.domains.security;

import lombok.*;
import net.dkahn.starter.tools.domain.BaseEntity;

import javax.persistence.*;

@Data
@Entity
@Table
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuthenticationAttempts extends BaseEntity{

    @Id
    @SequenceGenerator(name = "user_auth",sequenceName = "user_auth_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_auth")
    protected Long id;

    @OneToOne
    private User user;
    private int attempts;
}
