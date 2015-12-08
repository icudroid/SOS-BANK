package net.dkahn.starter.apps.webapps.common.utils.datatable;

import java.util.List;

/**
 * User: dimitri
 * Date: 09/02/15
 * Time: 14:19
 * Goal:
 */
public interface IBeanBuilder<SOURCE,DESTINATION>{
    DESTINATION build(SOURCE source);
    List<DESTINATION> build(List<SOURCE> source);
}
