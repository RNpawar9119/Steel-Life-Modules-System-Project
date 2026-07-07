//package com.jwt.tok.service;
//
//import java.util.List;
//
//import javax.transaction.Transactional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.jwt.tok.dao.QuotationDao;
//import com.jwt.tok.dto.QuotationRequest;
//import com.jwt.tok.model.Enquiry;
//import com.jwt.tok.model.QuotationItem;
//import com.jwt.tok.model.QuotationMaster;
//import com.jwt.tok.model.Slq;
//import com.jwt.tok.model.Slq1;
//import com.jwt.tok.model.Slq2;
//import com.jwt.tok.repository.EnquiryRepository;
//import com.jwt.tok.repository.QuotationRepository;
//import com.jwt.tok.repository.Slq1Repository;
//import com.jwt.tok.repository.Slq2Repository;
//import com.jwt.tok.repository.SlqRepository;
//
//@Service
//public class QuotationService {
//
//    @Autowired
//    private SlqRepository slqRepo;
//
//    @Autowired
//    private Slq1Repository slq1Repo;
//
//    @Autowired
//    private Slq2Repository slq2Repo;
//
//    @Autowired
//    private QuotationDao quotationDao;
//
//    @Transactional
//    public String saveQuotation(QuotationRequest request) {
//
//        Slq header = request.getHeader();
//
//        // Save Header
//        slqRepo.save(header);
//
//        // Save Items
//        for (Slq1 item : request.getItems()) {
//            item.setFyear(header.getFyear());
//            item.setDoctype(header.getDoctype());
//            item.setDocno(header.getDocno());
//            slq1Repo.save(item);
//        }
//
//        // Save Details
//        for (Slq2 detail : request.getDetails()) {
//            detail.setFyear(header.getFyear());
//            detail.setDoctype(header.getDoctype());
//            detail.setDocno(header.getDocno());
//            slq2Repo.save(detail);
//        }
//
//        // 🔥 CALL CALCULATION SP
//        for (Slq1 item : request.getItems()) {
//            quotationDao.calculateQuotation(
//                    header.getFyear(),
//                    header.getDoctype(),
//                    header.getDocno(),
//                    item.getItemno()
//            );
//        }
//
//        return "Quotation Saved Successfully";
//    }
//}