package net.dkahn.starter.tools.repository.pageable;

import net.dkahn.starter.tools.repository.filtering.IFiltering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * User: dimitri
 * Date: 10/02/15
 * Time: 11:05
 * Goal:
 */
public interface IPageable<T,FILTER extends IFiltering> {
    Page<T> find(FILTER filter, Sort sort, Pageable pageable);
}
