package net.dkahn.starter.domains.information;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name="type",
        discriminatorType=DiscriminatorType.STRING
)
public abstract class InformationPersonnel {
    @Id
    @SequenceGenerator(name = "info_perso", sequenceName = "info_perso_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "info_perso")
    protected Long id;
}
