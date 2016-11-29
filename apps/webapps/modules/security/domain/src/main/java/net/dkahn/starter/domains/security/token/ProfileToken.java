package net.dkahn.starter.domains.security.token;

import lombok.*;
import net.dkahn.starter.tools.domain.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by dev on 29/11/16.
 */
@Data
@Entity
@Table()
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProfileToken extends BaseEntity {

    @Id
    private String id;

    private String username;

    private String token;


}
