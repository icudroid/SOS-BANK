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
public class HistoryUserAuthenticationAttempts extends BaseEntity{
    @Id
    @SequenceGenerator(name = "user_auth_history",sequenceName = "user_auth_history_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_auth_history")
    protected Long id;

    @ManyToOne
    private User user;

    @Enumerated(value = EnumType.STRING)
    private AuthenticationStatus status;
}
