package net.dkahn.starter.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.dkahn.starter.domains.information.InformationPersonnel;
import net.dkahn.starter.tools.domain.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Entity
@Table(name = "app_user")
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "type",
        discriminatorType = DiscriminatorType.STRING
)
public abstract class Individu extends BaseEntity {


    @Id
    protected String id;

    protected LocalDateTime entreeEnRelation;

    @Enumerated(EnumType.STRING)
    protected Civilite civilite;

    protected String nom;
    protected String prenom;

    protected String nomDeNaissance;
    protected String villeDeNaissance;
    protected String paysDeNaissance;

    protected String nationalite;
    protected String residenceFiscale;

    @Embedded
    protected Adresse adresse;

    protected LocalDate naissance;

    @Embedded
    protected Commune communeDeNaissance;

    @OneToMany
    protected List<DocumentEntreeEnRelation> documentsEntreeEnRelation;

    @OneToMany
    protected List<MoyenDeContact> moyensDeContact;

    @OneToMany
    protected List<InformationPersonnel> informationsPersonnel;


}
