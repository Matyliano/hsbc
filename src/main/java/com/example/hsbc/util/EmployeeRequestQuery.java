package com.example.hsbc.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;


@RequiredArgsConstructor
@Repository
public class EmployeeRequestQuery {

    private final EntityManager entityManager;

    private final static String SELECT_QUERY =
            "select concat(e.name, ' ', e.surname) as name,\n" +
                    "from employee e \n";


    public List<Object[]> getEmployeesBySearchCriteria (Pageable pageable, RequestSearchEmployeeCriteria reportsCriteria) {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(SELECT_QUERY);
        queryBuilder.append(buildFilterQuery(reportsCriteria));
        queryBuilder.append("group by e.name, e.surname\n");

        addSorting(pageable, queryBuilder);

        Query query = entityManager.createNativeQuery(queryBuilder.toString());


        if(reportsCriteria.getName() != null) {
            query.setParameter("name", reportsCriteria.getName());
        }
        if(reportsCriteria.getSurname() != null) {
            query.setParameter("surname", reportsCriteria.getSurname());
        }
        if(reportsCriteria.getGrade() != null) {
            query.setParameter("grade", reportsCriteria.getGrade());
        }
        if(reportsCriteria.getSalary() != null) {
            query.setParameter("salary", reportsCriteria.getSalary());
        }

        return query.getResultList();
    }

    private String buildFilterQuery(RequestSearchEmployeeCriteria searchCriteria) {
        StringBuilder filterQuery = new StringBuilder();
        filterQuery.append(CollectionUtils.isEmpty(searchCriteria.getName()) ? "" : "and e.name in :name \n");
        filterQuery.append(CollectionUtils.isEmpty(searchCriteria.getSurname()) ? "" : "and e.surname in :surname \n");
        filterQuery.append(CollectionUtils.isEmpty(searchCriteria.getGrade()) ? "" : "and e.grade in :grade \n");
        filterQuery.append(CollectionUtils.isEmpty(searchCriteria.getSalary()) ? "" : "and e.salary in :salary \n");

        return filterQuery.toString();
    }

    private void addSorting(Pageable pageable, StringBuilder queryBuilder) {
        pageable.getSort();
        if (pageable.getSort().isSorted()) {
            Sort.Order order = pageable.getSort().iterator().next();
            String property = order.getProperty();
            queryBuilder.append(" order by ")
                    .append(property.equals("surname") ? "e.surname" : property).append(" ")
                    .append(order.getDirection().name());
        } else {
            queryBuilder.append(" order by e.surname");
        }
    }
}
