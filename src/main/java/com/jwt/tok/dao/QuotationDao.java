package com.jwt.tok.dao;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class QuotationDao {

    @Autowired
    private EntityManager entityManager;

    public void calculateQuotation(String fyear, String doctype, String docno, String itemno) {

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("SPSLQ22");

        query.registerStoredProcedureParameter("p_fyear", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_doctype", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_docno", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_itemno", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_uid", String.class, ParameterMode.IN);

        query.setParameter("p_fyear", fyear);
        query.setParameter("p_doctype", doctype);
        query.setParameter("p_docno", docno);
        query.setParameter("p_itemno", itemno);
        query.setParameter("p_uid", "999");

        query.execute();
    }
}