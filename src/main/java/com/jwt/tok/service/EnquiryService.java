package com.jwt.tok.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.tok.exception.ApiException;
import com.jwt.tok.model.Enquiry;
import com.jwt.tok.repository.DealerRepository;
import com.jwt.tok.repository.EnquiryRepository;

@Service
public class EnquiryService {

	private final EnquiryRepository enquiryRepository;
	private final ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private final DealerRepository dealerRepository;
	@Value("${file.upload-dir}")
	private String uploadDir;

	public EnquiryService(EnquiryRepository enquiryRepository, DealerRepository dealerRepository) {
		this.enquiryRepository = enquiryRepository;
		this.dealerRepository = dealerRepository;
	}

	public Enquiry create(Enquiry enquiry,
	        MultipartFile enquiryForm,
	        MultipartFile finalizationReport,
	        MultipartFile actualDimension,
	        MultipartFile design2D3D) throws IOException {

	    if (enquiry.getCustomerName() == null) {
	        throw new ApiException("Customer name is required");
	    }

	    if (enquiryForm != null && !enquiryForm.isEmpty()) {
	        enquiry.setEnquiryForm(saveFile(enquiryForm, "enquiry"));
	    }

	    if (finalizationReport != null && !finalizationReport.isEmpty()) {
	        enquiry.setFinalizationReport(saveFile(finalizationReport, "enquiry"));
	    }

	    if (actualDimension != null && !actualDimension.isEmpty()) {
	        enquiry.setActualDimension(saveFile(actualDimension, "enquiry"));
	    }

	    if (design2D3D != null && !design2D3D.isEmpty()) {
	        enquiry.setDesign2D3D(saveFile(design2D3D, "enquiry"));
	    }

	    enquiry.setStatus("NEW");

	    // 🔥 Business ID Generate
	    enquiry.setEnquiryId(generateEnquiryId());

	    return enquiryRepository.save(enquiry);
	}
	
	
	public List<Enquiry> getAll(String role, String dealerCode) {

		if ("Admin".equals(role)) {
			return enquiryRepository.findByStatus("NEW");
		}
		return enquiryRepository.findByStatusAndDealerCode("NEW", dealerCode);
	}

	// GET BY ID
//	public Enquiry getById(String enquiryId) {
//	    return enquiryRepository.findById(enquiryId)
//	        .orElseThrow(() -> new ApiException("Enquiry not found"));
//	}

	public Enquiry getByIdSecure(Long id, String role, String dealerCode) {

	    Enquiry enquiry = enquiryRepository.findById(id)
	            .orElseThrow(() -> new ApiException("Enquiry not found"));

	    if ("Admin".equals(role)) {
	        return enquiry;
	    }

	    if (!enquiry.getDealerCode().equals(dealerCode)) {
	        throw new ApiException("Unauthorized access");
	    }

	    return enquiry;
	}

//	public Enquiry updateSecure(Long enquiryId, String enquiryJson, MultipartFile enquiryForm,
//			MultipartFile finalizationReport, MultipartFile actualDimension, MultipartFile design2D3D, String role,
//			String dealerCode) throws IOException {
//
//		Enquiry enquiry = getByIdSecure(enquiryId, role, dealerCode);
//
//		Map<String, Object> map = mapper.readValue(enquiryJson, new TypeReference<Map<String, Object>>() {
//		});
//
//		enquiry.setCustomerName((String) map.get("customerName"));
//		enquiry.setPartyName((String) map.get("partyName"));
//		enquiry.setAddress((String) map.get("address"));
//		enquiry.setCity((String) map.get("city"));
//		enquiry.setContactNo((String) map.get("contactNo"));
//		enquiry.setEmail((String) map.get("email"));
//		enquiry.setProduct((String) map.get("product"));
//		enquiry.setReferredBy((String) map.get("referredBy"));
//		enquiry.setResource((String) map.get("resource"));
//
//		return enquiryRepository.save(enquiry);
//	}

	public Enquiry updateSecure(Long enquiryId,
	        String enquiryJson,
	        MultipartFile enquiryForm,
	        MultipartFile finalizationReport,
	        MultipartFile actualDimension,
	        MultipartFile design2D3D,
	        String role,
	        String dealerCode) throws IOException {

	    Enquiry enquiry = getByIdSecure(enquiryId, role, dealerCode);

	    Map<String, Object> map = mapper.readValue(
	            enquiryJson,
	            new TypeReference<Map<String, Object>>() {}
	    );

	    enquiry.setCustomerName((String) map.get("customerName"));
	    enquiry.setPartyName((String) map.get("partyName"));
	    enquiry.setAddress((String) map.get("address"));
	    enquiry.setCity((String) map.get("city"));
	    enquiry.setContactNo((String) map.get("contactNo"));
	    enquiry.setEmail((String) map.get("email"));
	    enquiry.setProduct((String) map.get("product"));
	    enquiry.setReferredBy((String) map.get("referredBy"));
	    enquiry.setResource((String) map.get("resource"));

	    // 🔥 FILE UPDATE LOGIC

	    if (enquiryForm != null && !enquiryForm.isEmpty()) {
	        enquiry.setEnquiryForm(saveFile(enquiryForm, "enquiry"));
	    }

	    if (finalizationReport != null && !finalizationReport.isEmpty()) {
	        enquiry.setFinalizationReport(saveFile(finalizationReport, "enquiry"));
	    }

	    if (actualDimension != null && !actualDimension.isEmpty()) {
	        enquiry.setActualDimension(saveFile(actualDimension, "enquiry"));
	    }

	    if (design2D3D != null && !design2D3D.isEmpty()) {
	        enquiry.setDesign2D3D(saveFile(design2D3D, "enquiry"));
	    }

	    return enquiryRepository.save(enquiry);
	}
	
	public void deleteSecure(Long id, String role, String dealerCode) {

	    Enquiry enquiry = getByIdSecure(id, role, dealerCode);

	    enquiryRepository.delete(enquiry);
	}
	
	
	private String saveFile(MultipartFile file, String folder) throws IOException {

		String dirPath = uploadDir + File.separator + folder;
		File dir = new File(dirPath);
		if (!dir.exists())
			dir.mkdirs();

		String cleanName = file.getOriginalFilename().replaceAll("\\s+", "_");
		String fileName = System.currentTimeMillis() + "_" + cleanName;

		Path path = Paths.get(dirPath, fileName);
		Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

		return "/uploads/" + folder + "/" + fileName;
	}

	public long getOrderCount(String role, String dealerCode) {

		if ("Admin".equals(role)) {
			return enquiryRepository.countByStatus("NEW");
		}

		return enquiryRepository.countByDealerCodeAndStatus(dealerCode, "NEW");
	}

	public void markEnquiryAsOrdered(Long id) {

	    Enquiry enquiry = enquiryRepository.findById(id)
	            .orElseThrow(() -> new ApiException("Enquiry not found"));

	    enquiry.setStatus("ORDERED");

	    enquiryRepository.save(enquiry);
	}

	public List<Map<String, Object>> getAll1WithDealerName(String role, String dealerCode) {

		List<Enquiry> enquiries;

		if ("Admin".equals(role)) {
			enquiries = enquiryRepository.findAll();
		} else {
			enquiries = enquiryRepository.findByDealerCode(dealerCode);
		}

		return enquiries.stream().map(enquiry -> {

			Map<String, Object> map = new HashMap<>();

			map.put("enquiryId", enquiry.getEnquiryId());
			map.put("customerName", enquiry.getCustomerName());
			map.put("partyName", enquiry.getPartyName());
			map.put("contactNo", enquiry.getContactNo());
			map.put("email", enquiry.getEmail());
			map.put("address", enquiry.getAddress());
			map.put("city", enquiry.getCity());
			map.put("date", enquiry.getDate());
			map.put("product", enquiry.getProduct());
			map.put("resource", enquiry.getResource());

			map.put("enquiryForm", enquiry.getEnquiryForm());
			map.put("actualDimension", enquiry.getActualDimension());
			map.put("design2D3D", enquiry.getDesign2D3D());
			map.put("finalizationReport", enquiry.getFinalizationReport());

			// 🔥 Dealer Name logic
			String dCode = enquiry.getDealerCode();
			map.put("dealerCode", dCode);

			if (dCode != null) {
				dealerRepository.findByDealerCode(dCode)
						.ifPresent(dealer -> map.put("dealerName", dealer.getDealerName()));
			} else {
				map.put("dealerName", "-");
			}

			return map;

		}).collect(Collectors.toList());
	}

	public List<Enquiry> getAllAssigned(String role, String dealerCode) {

		if ("Admin".equals(role)) {
			return enquiryRepository.findByStatus("ASSIGNED");
		}
		return enquiryRepository.findByStatusAndDealerCode("ASSIGNED", dealerCode);
	}

	private String generateEnquiryId() {

	    int year = LocalDate.now().getYear();

	    long count = enquiryRepository.countByEnquiryIdStartingWith("ENQ/" + year + "/");

	    long next = count + 1;

	    return String.format("ENQ/%d/%04d", year, next);
	}
	
	public List<Map<String, Object>> getEnquiriesByDateRangeWithDealer(
	        LocalDate fromDate,
	        LocalDate toDate,
	        String role,
	        String dealerCode
	) {

	    List<Enquiry> list = enquiryRepository.findByDateBetween(fromDate, toDate);

	    if (!"Admin".equals(role)) {
	        list = list.stream()
	                .filter(e -> dealerCode.equals(e.getDealerCode()))
	                .collect(Collectors.toList());
	    }

	    return list.stream().map(enquiry -> {

	        Map<String, Object> map = new HashMap<>();

	        map.put("id", enquiry.getId());
	        map.put("enquiryId", enquiry.getEnquiryId());
	        map.put("customerName", enquiry.getCustomerName());
	        map.put("partyName", enquiry.getPartyName());
	        map.put("contactNo", enquiry.getContactNo());
	        map.put("email", enquiry.getEmail());
	        map.put("address", enquiry.getAddress());
	        map.put("city", enquiry.getCity());
	        map.put("date", enquiry.getDate());
	        map.put("product", enquiry.getProduct());

	        map.put("enquiryForm", enquiry.getEnquiryForm());
	        map.put("actualDimension", enquiry.getActualDimension());
	        map.put("design2D3D", enquiry.getDesign2D3D());
	        map.put("finalizationReport", enquiry.getFinalizationReport());

	        // 🔥 IMPORTANT: Dealer Name add कर
	        String dCode = enquiry.getDealerCode();
	        map.put("dealerCode", dCode);

	        if (dCode != null) {
	            dealerRepository.findByDealerCode(dCode)
	                    .ifPresent(d -> map.put("dealerName", d.getDealerName()));
	        } else {
	            map.put("dealerName", "-");
	        }

	        return map;

	    }).collect(Collectors.toList());
	}
	
	public List<Enquiry> getAssignedByDateRange(
	        LocalDate fromDate,
	        LocalDate toDate,
	        String role,
	        String dealerCode
	) {

	    List<Enquiry> list =
	            enquiryRepository.findByStatusAndDateBetween(
	                    "ASSIGNED", fromDate, toDate
	            );

	    if ("Admin".equals(role)) {
	        return list;
	    }

	    return list.stream()
	            .filter(e -> dealerCode.equals(e.getDealerCode()))
	            .collect(Collectors.toList());
	}
}