package net.dkahn.starter.services.individu.impl;

import net.dkahn.starter.domains.*;
import net.dkahn.starter.domains.information.*;
import net.dkahn.starter.services.individu.IClientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableJpaRepositories
@EnableTransactionManagement
@EntityScan(basePackages = {"net.dkahn.starter"})
@ComponentScan
public class ClientServiceTest {
    @Autowired
    private IClientService clientService;

    @Transactional
    @Test
    public void save() throws Exception {
        Client client = new Client();
        client.setNom("KAHN");
        client.setCivilite(Civilite.MR);
        client.setEntreeEnRelation(LocalDateTime.now());
        client.setNaissance(LocalDate.of(1977,5,1));
        client.setNationalite("FR");
        client.setNomDeNaissance("KAHN");
        client.setPaysDeNaissance("FR");
        client.setPrenom("Dimitri");
        client.setResidenceFiscale("FR");
        client.setVilleDeNaissance("Paris");

        Adresse address = new Adresse();
        address.setAdresse("11 rue du test");
        address.setCodePostal("75001");
        address.setPays("FR");
        address.setValide(Boolean.TRUE);
        address.setVille("PARIS");
        client.setAdresse(address);

        List<MoyenDeContact> contacts = new ArrayList<>();
        contacts.add(createMoyenContact(CanalContact.EMAIL,"dimitri@d-kahn.net"));
        contacts.add(createMoyenContact(CanalContact.MOBILE,"+33679215487"));
        client.setMoyensDeContact(contacts);

        List<InformationPersonnel> informationsPersonnel = new ArrayList<>();
        ContratTravailInformation contratTravailInformation = new ContratTravailInformation();
        contratTravailInformation.setContractTravail(ContractTravail.CDI);
        informationsPersonnel.add(contratTravailInformation);
        FonctionInformation fonctionInformation = new FonctionInformation();
        fonctionInformation.setFonction("Fonction");
        informationsPersonnel.add(fonctionInformation);
        PatrimoineInformation patrimoineInformation = new PatrimoineInformation();
        patrimoineInformation.setPatrimoine(Patrimoine.DE_0_100000);
        informationsPersonnel.add(patrimoineInformation);
        RevenuFoyerInformation revenuFoyerInformation = new RevenuFoyerInformation();
        revenuFoyerInformation.setRevenuFoyer(RevenuFoyer.DE_0_25000);
        informationsPersonnel.add(revenuFoyerInformation);
        RevenuInformation revenuInformation = new RevenuInformation();
        revenuInformation.setRevenu(1500);
        informationsPersonnel.add(revenuInformation);
        SecteurInformation secteurInformation = new SecteurInformation();
        secteurInformation.setSecteur(Secteur.ACTIVITES_IMMOBILIÈRES);
        informationsPersonnel.add(secteurInformation);
        SituationPersonnelInformation situationPersonnelInformation = new SituationPersonnelInformation();
        situationPersonnelInformation.setSituationPersonnel(SituationPersonnel.CHAUFFEURS);
        informationsPersonnel.add(situationPersonnelInformation);
        client.setInformationsPersonnel(informationsPersonnel);

        List<DocumentEntreeEnRelation> documentsEER = new ArrayList<>();
        DocumentEntreeEnRelation doc = new DocumentEntreeEnRelation();
        doc.setOriginalFilename("test.txt");
        doc.setOriginalSize(2000);
        doc.setPassphrase("12345");
        doc.setPath("/tmp");
        doc.setType(TypeDocumentEntreEnRelation.CARTE_IDENTITE);
        documentsEER.add(doc);
        client.setDocumentsEntreeEnRelation(documentsEER);

        Commune commune = new Commune();
        commune.setCodeDepartement("75");
        commune.setCodeGeographique("123456");
        commune.setCodePostal("75000");
        commune.setCommune("PARIS");
        client.setCommuneDeNaissance(commune);


        Client save = (Client) clientService.save(client);

    }


    private MoyenDeContact createMoyenContact(CanalContact type, String contact) {
        MoyenDeContact moyenDeContact = new MoyenDeContact();
        moyenDeContact.setCanal(type);
        moyenDeContact.setContact(contact);
        return moyenDeContact;
    }



}
