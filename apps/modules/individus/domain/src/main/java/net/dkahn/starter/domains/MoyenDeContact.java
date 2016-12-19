package net.dkahn.starter.domains;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
public class MoyenDeContact {
    @Id
    @SequenceGenerator(name = "moyen_contact",sequenceName = "moyen_contact_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "moyen_contact")
    private Long id;

    private CanalContact canal;
    private String contact;
}
