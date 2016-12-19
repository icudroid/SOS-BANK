package net.dkahn.starter.domains;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
public class DocumentEntreeEnRelation {
    @Id
    private Long id;
    @Enumerated(EnumType.STRING)
    private TypeDocumentEntreEnRelation type;
    private String originalFilename;
    private long originalSize;
    private String passphrase;

    private String path;

}
