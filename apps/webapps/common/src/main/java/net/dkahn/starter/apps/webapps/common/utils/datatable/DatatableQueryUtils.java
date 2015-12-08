package net.dkahn.starter.apps.webapps.common.utils.datatable;

import net.dkahn.starter.apps.webapps.common.command.datatable.IFilteringCommand;
import net.dkahn.starter.apps.webapps.common.command.datatable.query.DataTableOrder;
import net.dkahn.starter.apps.webapps.common.command.datatable.query.DataTableQuery;
import net.dkahn.starter.apps.webapps.common.command.datatable.query.DataTableSearch;
import net.dkahn.starter.apps.webapps.common.command.datatable.response.DataTableResponse;
import net.dkahn.starter.tools.repository.filtering.IFiltering;
import net.dkahn.starter.tools.repository.pageable.IPageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: dimitri
 * Date: 10/02/15
 * Time: 11:54
 * Goal:
 */
public class DatatableQueryUtils<FILTER_CLASS extends IFiltering>{

    static public <
            OBJECT,
            DTO,
            ADDITION_FORM extends IFilteringCommand<FILTER>,
            FILTER extends IFiltering
            >
    DataTableResponse<DTO>
        query(DataTableQuery<ADDITION_FORM> query, List<String> relationObject, IBeanBuilder<OBJECT, DTO> builder, IPageable pageableRepo) throws Exception{
        DatatableQueryUtils<FILTER> queryUtils = new DatatableQueryUtils<FILTER>();
        return queryUtils.findByQuery(query,relationObject,builder,pageableRepo);
    }


    static public <
            OBJECT,
            DTO,
            ADDITION_FORM extends IFilteringCommand<FILTER>,
            FILTER extends IFiltering
            >
    DataTableResponse<DTO>
    queryDTO(DataTableQuery<ADDITION_FORM> query, List<String> relationObject, IPageable pageableRepo) throws Exception{
        DatatableQueryUtils<FILTER> queryUtils = new DatatableQueryUtils<FILTER>();
        return queryUtils.findByQueryDTO(query, relationObject, pageableRepo);
    }


    static public List<String> computeProperties(String properties){
        return Arrays.asList(properties.split(","));
    }



    public <
            DTO,
            ADDITION_FORM extends IFilteringCommand<FILTER>,
            FILTER extends IFiltering
            >
    DataTableResponse<DTO>
    findByQueryDTO(DataTableQuery<ADDITION_FORM> query, List<String> relationObject, IPageable pageableRepo) throws Exception {

        DataTableResponse<DTO> res = new DataTableResponse();
        res.setDraw(query.getDraw());

        DataTableSearch searchObj = query.getSearch();
        String value = searchObj.getValue();


        Integer length = query.getLength();
        Integer start = query.getStart();

        Sort sort = null;

        for (DataTableOrder dataTableOrder : query.getOrder()) {
            Sort orders = new Sort(dataTableOrder.toDirection(), computeProperties(relationObject.get(dataTableOrder.getColumn())));
            if(sort==null){
                sort = orders;
            }else{
                sort.and(orders);
            }
        }

        int page = start / length;
        if(length ==-1){
            page = 0;
            length = Integer.MAX_VALUE;
        }

        Pageable pageable = new PageRequest(page,length,sort);

        ADDITION_FORM additionalForm = query.getAdditionalForm();
        FILTER filter = null;
        if(additionalForm!=null) {
            filter = additionalForm.toFilter();
        }else{
            filter = (FILTER) newFilter().defaultFilter(value);
        }

        Page<DTO> content = pageableRepo.find(filter, sort, pageable);

        res.setData(content.getContent());
        res.setRecordsFiltered(content.getTotalElements());
        res.setRecordsTotal(content.getTotalElements());

        return res;
    }



    public <
            OBJECT,
            DTO,
            ADDITION_FORM extends IFilteringCommand<FILTER>,
            FILTER extends IFiltering
            >
    DataTableResponse<DTO>
    findByQuery(DataTableQuery<ADDITION_FORM> query, List<String> relationObject, IBeanBuilder<OBJECT, DTO> builder, IPageable pageableRepo) throws Exception {

        DataTableResponse<DTO> res = new DataTableResponse();
        res.setDraw(query.getDraw());

        DataTableSearch searchObj = query.getSearch();
        String value = searchObj.getValue();


        Integer length = query.getLength();
        Integer start = query.getStart();
        List<Sort.Order> orders = new ArrayList<>();


        List<DataTableOrder> orderObj = query.getOrder();
        orders.addAll(orderObj.stream().map(dataTableOrder -> new Sort.Order(dataTableOrder.toDirection(), relationObject.get(dataTableOrder.getColumn()))).collect(Collectors.toList()));
        Sort sort = new Sort(orders);

        int page = start / length;
        if(length ==-1){
            page = 0;
            length = Integer.MAX_VALUE;
        }

        Pageable pageable = new PageRequest(page,length,sort);

        ADDITION_FORM additionalForm = query.getAdditionalForm();
        FILTER filter = null;
        if(additionalForm!=null) {
            filter = additionalForm.toFilter();
        }else{
            filter = (FILTER) newFilter().defaultFilter(value);
        }

        Page<OBJECT> objects = pageableRepo.find(filter,sort, pageable);

        List<DTO> content = builder.build(objects.getContent());

        res.setData(content);
        res.setRecordsFiltered(objects.getTotalElements());
        res.setRecordsTotal(objects.getTotalElements());

        return res;
    }

    private FILTER_CLASS newFilter() throws IllegalAccessException, InstantiationException {
        return (FILTER_CLASS) ((Class)((ParameterizedType)this.getClass().
                getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
    }
}
