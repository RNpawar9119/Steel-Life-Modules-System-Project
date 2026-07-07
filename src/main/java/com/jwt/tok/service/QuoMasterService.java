package com.jwt.tok.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jwt.tok.model.QuoMaster;
import com.jwt.tok.model.QuoMasterItem;
import com.jwt.tok.model.QuoMasterItem2;
import com.jwt.tok.repository.QuoMasterRepository;
import com.jwt.tok.repository.QuotationSPRepository;

@Service
public class QuoMasterService {

    private final QuoMasterRepository repo;
    private final QuotationSPRepository spRepo;

    public QuoMasterService(QuoMasterRepository repo ,QuotationSPRepository spRepo) {
        this.repo = repo;
        this.spRepo = spRepo;
    }
    
    //-------------stored procedures call --------------------
    
    public Object addQuotation(QuoMaster quo) {

        String FYEAR = "2024";
        String DOCTYPE = "SLQ";

        // 1. DOCNO GENERATE
        Long docNo = spRepo.generateDocNo();

        // 2. INSERT MASTER
        spRepo.insertSlqMaster(FYEAR, DOCTYPE, docNo, quo);

        int itemNo = 1;

        // 3. LOOP ITEMS
        for (QuoMasterItem item : quo.getItems()) {

            // SPSLQ20
            spRepo.callSPSLQ20(FYEAR, DOCTYPE, docNo, itemNo, item);

            // INSERT SLQ2
            spRepo.insertSlq2(FYEAR, DOCTYPE, docNo, itemNo, item.getSrNo(), item);

            // CALCULATION
            spRepo.callSPSLQ22(FYEAR, DOCTYPE, docNo, itemNo);

            itemNo++;
        }

        // 4. FINAL TOTAL
        Object[] total = spRepo.callSPSLQ26(FYEAR, DOCTYPE, docNo, quo.getType());

        // 5. SAVE JPA (optional)
        quo.setQuotationNo(String.valueOf(docNo));
        repo.save(quo);

        return total;
    }
//---------------------------------------------------------------

    // add data
    public QuoMaster save(QuoMaster quo) {

        if (quo.getItems() != null) {
            for (QuoMasterItem item : quo.getItems()) {
                item.setQuoMaster(quo);
            }
        }

        if (quo.getImageItems() != null) {
            for (QuoMasterItem2 item : quo.getImageItems()) {
                item.setQuoMaster(quo);
            }
        }

        return repo.save(quo);
    }

    //all get
    public List<QuoMaster> getAll() {
        return repo.findAll();
    }

    // getid
    public QuoMaster getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Quotation not found"));
    }

    // update
    public QuoMaster update(Long id, QuoMaster updated) {

        QuoMaster existing = getById(id);

        existing.setQuotationNo(updated.getQuotationNo());
        existing.setDate(updated.getDate());
        existing.setCustomerName(updated.getCustomerName());
        existing.setType(updated.getType());
        existing.setCustTypeNo(updated.getCustTypeNo());
        existing.setAddress(updated.getAddress());
        existing.setEmail(updated.getEmail());
        existing.setMobile1(updated.getMobile1());
        existing.setMobile2(updated.getMobile2());
        existing.setSalesExecutive(updated.getSalesExecutive());
        existing.setDescription(updated.getDescription());

        // cost
        existing.setMc(updated.getMc());
        existing.setCc(updated.getCc());
        existing.setCt(updated.getCt());
        existing.setDc(updated.getDc());
        existing.setDt(updated.getDt());
        existing.setDisOnBOM(updated.getDisOnBOM());
        existing.setHardwareDisc(updated.getHardwareDisc());
        existing.setCd(updated.getCd());
        existing.setDd(updated.getDd());

        // clear old
        existing.getItems().clear();
        existing.getImageItems().clear();

        //add new items
        if (updated.getItems() != null) {
            for (QuoMasterItem item : updated.getItems()) {
                item.setQuoMaster(existing);
                existing.getItems().add(item);
            }
        }

        if (updated.getImageItems() != null) {
            for (QuoMasterItem2 item : updated.getImageItems()) {
                item.setQuoMaster(existing);
                existing.getImageItems().add(item);
            }
        }

        return repo.save(existing);
    }

    //Delete
    public void delete(Long id) {
        repo.deleteById(id);
    }
}