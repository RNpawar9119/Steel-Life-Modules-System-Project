package com.jwt.tok.service;

import java.io.File;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.tok.repository.OrderRemarkRepository;
import com.jwt.tok.exception.ApiException;
import com.jwt.tok.model.Dealer;
import com.jwt.tok.model.Enquiry;
import com.jwt.tok.model.OrderItem;
import com.jwt.tok.model.OrderRemark;
import com.jwt.tok.model.Orders;
import com.jwt.tok.model.enums.OrderWorkflowStatus;
import com.jwt.tok.repository.DealerRepository;
import com.jwt.tok.repository.EnquiryRepository;
import com.jwt.tok.repository.OrderItemRepository;
import com.jwt.tok.repository.OrderRepository;
import com.jwt.tok.util.JwtUtil;

@Service
public class OrderService {

	private final OrderRepository repo;
	private final OrderItemRepository itemRepo;

	@Autowired
	private EnquiryRepository enquiryRepository;
	@Autowired
	private DealerRepository dealerRepository;

	@Autowired
	private OrderRemarkRepository remarkRepo;
	@Autowired
	private JwtUtil jwtUtil;

	@Value("${file.upload-dir}")
	private String uploadDir;

	private final ObjectMapper mapper = new ObjectMapper();

	public OrderService(OrderRepository repo, OrderItemRepository itemRepo, EnquiryRepository enquiryRepository,
			OrderRemarkRepository remarkRepo) {
		this.repo = repo;
		this.itemRepo = itemRepo;
		this.enquiryRepository = enquiryRepository;
		this.remarkRepo = remarkRepo;
	}

	// ---------------------------------------------------------------
	public Orders saveOrder(Orders order) {

		if(order.getOrderId()==null){
		    order.setOrderId(generateOrderId());
		}
		
	    Orders savedOrder = repo.save(order);

	    if (order.getEnquiryId() != null) {
	        markEnquiryAsAssigned(order.getEnquiryId());
	    }

	    return savedOrder;
	}

	private String saveFile(MultipartFile file) throws Exception {

		String orderDir = uploadDir + "/orders";
		File dir = new File(orderDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		Path filePath = Paths.get(orderDir, fileName);

		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		// ✅ VERY IMPORTANT: correct DB path
		return "/uploads/orders/" + fileName;
	}

	public List<Orders> getAll(String role, String dealerCode) {

		if ("Admin".equals(role) || "Employee".equals(role)) {
			return repo.findAll();
		}

		if ("Dealer".equals(role)) {
			return repo.findByDealerCode(dealerCode);
		}

		throw new ApiException("Invalid role");
	}

	public Orders getById(Long id) {
		return repo.findById(id).orElseThrow(() -> new ApiException("Order not found with id: " + id));
	}

	public void delete(Long id) {
		if (!repo.existsById(id)) {
			throw new ApiException("Order not found with id: " + id);
		}
		repo.deleteById(id);
	}

	// ---------------------------------------------------------------------
	public long getOrderCount(String role, String dealerCode) {

		if ("Admin".equals(role)) {
			return repo.count();
		}
		return repo.countByDealerCode(dealerCode);
	}

	public Orders cancelOrder(Long  id) {
		Orders order = repo.findById(id).orElseThrow(() -> new ApiException("Order not found"));

		order.setStatus("CANCELLED");
		return repo.save(order);
	}

	public long getCancelledOrderCount() {
		return repo.countByStatus("CANCELLED");
	}

	public List<Orders> getAllCancelledOrders() {
		return repo.findByStatus("CANCELLED");
	}

	// -------------------------------------------------------------------
	public Orders updateOrder(Long  id, Orders updated) {

		Orders existing = repo.findById(id).orElseThrow(() -> new ApiException("Order not found with id: " + id));

		existing.setPartyName(updated.getPartyName());
		existing.setContactNo(updated.getContactNo());
		existing.setCustomerName(updated.getCustomerName());
		existing.setAddress(updated.getAddress());
		existing.setEmail(updated.getEmail());
		existing.setCity(updated.getCity());
		existing.setDate(updated.getDate());
		

		if (updated.getItems() != null) {
			for (OrderItem item : updated.getItems()) {

				if (item.getId() != null) {
					OrderItem dbItem = itemRepo.findById(item.getId())
							.orElseThrow(() -> new ApiException("Item not found"));

					dbItem.setRoom(item.getRoom());
					dbItem.setProduct(item.getProduct());
				}

			}
		}

		return repo.save(existing);
	}

	// ---------------------------------------------------------------------
	private void markEnquiryAsAssigned(Long enquiryId) {
		Enquiry enquiry = enquiryRepository.findById(enquiryId)
				.orElseThrow(() -> new ApiException("Enquiry not found with id: " + enquiryId));

		enquiry.setStatus("ASSIGNED");
		enquiryRepository.save(enquiry);
	}

	public Orders updateOrderWithFiles(Long  id, Orders updated, MultipartFile enquiryForm,
			MultipartFile finalizationReport, MultipartFile actualDimension, MultipartFile design2D3D)
			throws Exception {

		Orders existing = getById(id);

		existing.setPartyName(updated.getPartyName());
		existing.setContactNo(updated.getContactNo());
		existing.setCustomerName(updated.getCustomerName());
		existing.setAddress(updated.getAddress());
		existing.setEmail(updated.getEmail());
		existing.setCity(updated.getCity());
		existing.setDate(updated.getDate());
		existing.setProductName(updated.getProductName());
		existing.setEnquiryIds(updated.getEnquiryIds());
		// existing.setDealerRemark(updated.getDealerRemark());

		if (enquiryForm != null && !enquiryForm.isEmpty())
			existing.setEnquiryForm(saveFile(enquiryForm));

		if (finalizationReport != null && !finalizationReport.isEmpty())
			existing.setFinalizationReport(saveFile(finalizationReport));

		if (actualDimension != null && !actualDimension.isEmpty())
			existing.setActualDimension(saveFile(actualDimension));

		if (design2D3D != null && !design2D3D.isEmpty())
			existing.setDesign2D3D(saveFile(design2D3D));
		if (existing.getWorkflowStatus() == OrderWorkflowStatus.DEALER_REMARKED) {
			existing.setWorkflowStatus(OrderWorkflowStatus.SENT_TO_DEALER);
		}

		return repo.save(existing);
//		return repo.save(existing);
	}

	public Orders saveOrderWithFiles(Orders order, MultipartFile enquiryForm, MultipartFile finalizationReport,
			MultipartFile actualDimension, MultipartFile design2D3D, String token) throws Exception {

		String role = jwtUtil.extractRole(token);
		String dealerCode = jwtUtil.extractDealerCode(token);
		
		if(order.getOrderId()==null){
		    order.setOrderId(generateOrderId());
		}

		// 🔥 DEALER LOGIC
		if ("Dealer".equals(role)) {
			Dealer dealer = dealerRepository.findByDealerCode(dealerCode)
					.orElseThrow(() -> new ApiException("Dealer not found"));

			order.setDealerCode(dealerCode);
			order.setAssignedDealer(dealer);
		}
		if (order.getEnquiryId() != null) {

			Enquiry enquiry = enquiryRepository.findById(order.getEnquiryId())
					.orElseThrow(() -> new ApiException("Enquiry not found"));

			order.setEnquiry(enquiry);
			//order.setEnquiry(enquiry);
			order.setProductName(order.getProductName());
			order.setEnquiryIds(order.getEnquiryIds());		}

		// 🔥 FILE SAVE
		if (enquiryForm != null && !enquiryForm.isEmpty())
			order.setEnquiryForm(saveFile(enquiryForm));

		if (finalizationReport != null && !finalizationReport.isEmpty())
			order.setFinalizationReport(saveFile(finalizationReport));

		if (actualDimension != null && !actualDimension.isEmpty())
			order.setActualDimension(saveFile(actualDimension));

		if (design2D3D != null && !design2D3D.isEmpty())
			order.setDesign2D3D(saveFile(design2D3D));

		Orders saved = repo.save(order);
		if (order.getEnquiryId() != null) {
			markEnquiryAsAssigned(order.getEnquiryId());
		}

		return saved;
	}

	public Orders addDealerRemark(Long  id, String remark, String username) {

		Orders order = getById(id);

		OrderRemark newRemark = new OrderRemark();
		newRemark.setRemark(remark);
		newRemark.setCreatedBy(username);
		newRemark.setOrder(order);

		remarkRepo.save(newRemark);

		order.setWorkflowStatus(OrderWorkflowStatus.DEALER_REMARKED);

		return repo.save(order);
	}

	
	
	public Orders addRemark(Long id, String remark, String username, String role) {

	    Orders order = getById(id);

	    OrderRemark newRemark = new OrderRemark();

	    newRemark.setRemark(remark);
	    newRemark.setCreatedBy(username);
	    newRemark.setRole(role);
	    newRemark.setOrder(order);

	    remarkRepo.save(newRemark);

	    // workflow logic
	    if ("Dealer".equalsIgnoreCase(role)) {
	        order.setWorkflowStatus(OrderWorkflowStatus.DEALER_REMARKED);
	    } else {
	        order.setWorkflowStatus(OrderWorkflowStatus.SENT_TO_DEALER);
	    }

	    return repo.save(order);
	}
	
	public List<OrderRemark> getRemarksByOrderId(Long orderId) {

	    // order exists check (optional but recommended)
	    getById(orderId);

	    return remarkRepo.findByOrderIdOrderByCreatedAtDesc(orderId);
	}

	public Orders sendToProduction(Long  id) {
		Orders order = getById(id);

		if (order.getWorkflowStatus() != OrderWorkflowStatus.DEALER_APPROVED) {
			throw new ApiException("Dealer approval required before production");
		}

		order.setWorkflowStatus(OrderWorkflowStatus.READY_FOR_PRODUCTION);
		order.setStatus("IN_PRODUCTION");

		return repo.save(order);
	}

	public Orders updateWorkflowStatus(Long  id, OrderWorkflowStatus status) {
		Orders order = getById(id);
		order.setWorkflowStatus(status);
		return repo.save(order);
	}

	public List<Orders> getOrdersByWorkflowStatus(OrderWorkflowStatus status) {
		return repo.findByWorkflowStatus(status);
	}
	
	public List<Orders> getByWorkflowStatus(String status) {

	    OrderWorkflowStatus workflowStatus;

	    try {
	        workflowStatus = OrderWorkflowStatus.valueOf(status);
	    } catch (IllegalArgumentException e) {
	        throw new ApiException("Invalid workflow status: " + status);
	    }

	    return repo.findByWorkflowStatus(workflowStatus);
	}
	
	public List<Orders> getByOrderStatus(String status) {
	    return repo.findByStatus(status);
	}
	
	private String generateOrderId() {

	    int year = java.time.LocalDate.now().getYear();

	    String prefix = "ORD/" + year + "/";

	    long count = repo.countByOrderIdStartingWith(prefix);

	    long next = count + 1;

	    return String.format("ORD/%d/%04d", year, next);
	}
	
	public List<Orders> getOrdersByDateRange(String role, String dealerCode, LocalDate from, LocalDate to) {

	    if (from == null || to == null) {
	        throw new ApiException("From Date and To Date are required");
	    }

	    if ("Admin".equals(role) || "Employee".equals(role)) {
	        return repo.findByDateBetween(from, to);
	    }

	    if ("Dealer".equals(role)) {
	        return repo.findByDateBetweenAndDealerCode(from, to, dealerCode);
	    }

	    throw new ApiException("Invalid role");
	}
	
	
	public List<Orders> getCompletedOrdersByDate(LocalDate from, LocalDate to) {

	    return repo.findByStatusAndDateBetween("IN_PRODUCTION", from, to);
	}
	
	public List<Orders> getCancelledOrdersByDate(LocalDate fromDate, LocalDate toDate) {

	    if (fromDate == null || toDate == null) {
	        throw new ApiException("Both dates are required");
	    }

	    return repo.findCancelledOrdersBetweenDates(fromDate, toDate);
	}
	
	
	public List<Map<String, Object>> getOrdersWithDealerName(
	        String role,
	        String dealerCode,
	        LocalDate from,
	        LocalDate to
	) {

	    List<Orders> orders;

	    if ("Admin".equals(role) || "Employee".equals(role)) {
	        orders = repo.findByDateBetween(from, to);
	    } else {
	        orders = repo.findByDateBetweenAndDealerCode(from, to, dealerCode);
	    }

	    return orders.stream().map(order -> {

	        Map<String, Object> map = new HashMap<>();

	        map.put("id", order.getId());
	        map.put("orderId", order.getOrderId());
	        map.put("customerName", order.getCustomerName());
	        map.put("contactNo", order.getContactNo());
	        map.put("city", order.getCity());
	        map.put("date", order.getDate());
	        map.put("status", order.getStatus());
	        map.put("workflowStatus", order.getWorkflowStatus());

	        if (order.getEnquiry() != null) {
	            map.put("enquiryId", order.getEnquiry().getEnquiryId());
	        }

	        // 🔥 DEALER FIX (IMPORTANT)
	        String dCode = order.getDealerCode();
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
	
}