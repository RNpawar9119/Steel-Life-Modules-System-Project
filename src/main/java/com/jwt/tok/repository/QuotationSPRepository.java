package com.jwt.tok.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.jwt.tok.model.QuoMaster;
import com.jwt.tok.model.QuoMasterItem;

@Repository
public class QuotationSPRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // DOCNO GENERATE
    public Long generateDocNo() {
        Query query = entityManager.createNativeQuery(
            "SELECT IFNULL(MAX(CAST(docno AS SIGNED)),0)+1 FROM slq"
        );
        return ((Number) query.getSingleResult()).longValue();
    }

    // INSERT MASTER
    public void insertSlqMaster(String fyear, String doctype, Long docno, QuoMaster q) {
        entityManager.createNativeQuery(
            "INSERT INTO slq (FYEAR, DOCTYPE, DOCNO, DOCDT, T0, T1, T3, T6) " +
            "VALUES (:fyear, :doctype, :docno, NOW(), :name, :mobile, :email, :exec)"
        )
        .setParameter("fyear", fyear)
        .setParameter("doctype", doctype)
        .setParameter("docno", docno)
        .setParameter("name", q.getCustomerName())
        .setParameter("mobile", q.getMobile1())
        .setParameter("email", q.getEmail())
        .setParameter("exec", q.getSalesExecutive())
        .executeUpdate();
    }

    // SPSLQ20 CALL
    public void callSPSLQ20(String fyear, String doctype, Long docno,
                            int itemNo, QuoMasterItem item) {

        entityManager.createNativeQuery("CALL SPSLQ20(:fyear,:doctype,:docno,:lno,:loc,:desc,:wdhq)")
                .setParameter("fyear", fyear)
                .setParameter("doctype", doctype)
                .setParameter("docno", docno)
                .setParameter("lno", itemNo)
                .setParameter("loc", item.getLocation())
                .setParameter("desc", item.getDescription())
                .setParameter("wdhq", item.getSize())
                .executeUpdate();
    }

    // INSERT INTO SLQ2 (BOM ROW)
    public void insertSlq2(String fyear, String doctype, Long docno,
                           int itemNo, int srNo, QuoMasterItem item) {

        entityManager.createNativeQuery(
            "INSERT INTO slq2 (FYEAR, DOCTYPE, DOCNO, ITEMNO, SRNO, LOC, NAME1, WDHQ) " +
            "VALUES (:fyear,:doctype,:docno,:itemno,:srno,:loc,:desc,:wdhq)"
        )
        .setParameter("fyear", fyear)
        .setParameter("doctype", doctype)
        .setParameter("docno", docno)
        .setParameter("itemno", itemNo)
        .setParameter("srno", srNo)
        .setParameter("loc", item.getLocation())
        .setParameter("desc", item.getDescription())
        .setParameter("wdhq", item.getSize())
        .executeUpdate();
    }

    // SPSLQ22
    public void callSPSLQ22(String fyear, String doctype, Long docno, int itemNo) {
        entityManager.createNativeQuery("CALL SPSLQ22(:fyear,:doctype,:docno,:itemno,'999')")
                .setParameter("fyear", fyear)
                .setParameter("doctype", doctype)
                .setParameter("docno", docno)
                .setParameter("itemno", itemNo)
                .executeUpdate();
    }

    // SPSLQ25
    public Object[] callSPSLQ25(String fyear, String doctype, Long docno, int itemNo) {
        return (Object[]) entityManager
                .createNativeQuery("CALL SPSLQ25(:fyear,:doctype,:docno,:itemno,'999')")
                .setParameter("fyear", fyear)
                .setParameter("doctype", doctype)
                .setParameter("docno", docno)
                .setParameter("itemno", itemNo)
                .getSingleResult();
    }

    // SPSLQ26
    public Object[] callSPSLQ26(String fyear, String doctype, Long docno, String type) {
        return (Object[]) entityManager
                .createNativeQuery("CALL SPSLQ26(:fyear,:doctype,:docno,:type)")
                .setParameter("fyear", fyear)
                .setParameter("doctype", doctype)
                .setParameter("docno", docno)
                .setParameter("type", type)
                .getSingleResult();
    }
}