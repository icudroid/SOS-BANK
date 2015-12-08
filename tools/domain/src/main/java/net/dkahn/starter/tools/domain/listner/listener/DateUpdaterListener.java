package net.dkahn.starter.tools.domain.listner.listener;

import net.dkahn.starter.tools.domain.BaseEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;


public class DateUpdaterListener {

    @PrePersist
    public void setDateCreation(BaseEntity entity) {
        entity.setCreationDate(LocalDateTime.now());
    }

    @PreUpdate
    public void setDateUpdate(BaseEntity entity) {
        entity.setUpdateDate(LocalDateTime.now());
    }
}
