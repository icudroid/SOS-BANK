package net.dkahn.starter.domains.security;

import lombok.*;
import net.dkahn.starter.tools.domain.BaseEntity;
import net.dkahn.starter.tools.domain.converter.IntArrayToStringConverter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.UUIDGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class Pinpad extends BaseEntity{

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    @Convert(converter = IntArrayToStringConverter.class)
    private List<Integer> correspondance;

    private boolean provided;
    
}
