package net.dkahn.starter.tools.domain;


import net.dkahn.starter.tools.domain.converter.LocalDateTimePersistenceConverter;
import net.dkahn.starter.tools.domain.listner.listener.DateUpdaterListener;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Convert;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@ToString
@EntityListeners(DateUpdaterListener.class)
public abstract class BaseEntity {


    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime creationDate;

    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime updateDate;

}