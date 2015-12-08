package net.dkahn.starter.apps.webapps.common.command.datatable;


import net.dkahn.starter.tools.repository.filtering.IFiltering;

/**
 * User: dimitri
 * Date: 09/02/15
 * Time: 14:05
 * Goal:
 */
public interface IFilteringCommand<FILTER extends IFiltering> {
    FILTER toFilter();
}
