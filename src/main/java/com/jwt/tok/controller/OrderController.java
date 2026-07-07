package com.jwt.tok.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jwt.tok.model.OrderRemark;
import com.jwt.tok.model.Orders;
import com.jwt.tok.model.enums.OrderWorkflowStatus;
import com.jwt.tok.response.ApiResponse;
import com.jwt.tok.service.OrderService;
import com.jwt.tok.util.JwtUtil;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrderController {

	private final OrderService orderService;
	@Autowired
	private JwtUtil jwtUtil;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}
	// -------------------------------------------------------------------------------
	@PostMapping(value = "/save", consumes = "multipart/form-data")
	@PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
	public ResponseEntity<ApiResponse<Orders>> saveOrder(HttpServletRequest request, @RequestPart("order") Orders order,
			@RequestPart(value = "enquiryForm", required = false) MultipartFile enquiryForm,
			@RequestPart(value = "finalizationReport", required = false) MultipartFile finalizationReport,
			@RequestPart(value = "actualDimension", required = false) MultipartFile actualDimension,
			@RequestPart(value = "design2D3D", required = false) MultipartFile design2D3D) throws Exception {

		String token = request.getHeader("Authorization").substring(7);

		Orders saved = orderService.saveOrderWithFiles(order, enquiryForm, finalizationReport, actualDimension,
				design2D3D, token);

		return ResponseEntity.ok(ApiResponse.success("Order saved successfully", saved));
	}

//-------------------------------------------------------------------------------------------------
	@GetMapping("/all")
	@PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
	public ResponseEntity<ApiResponse<List<Orders>>> getAll(HttpServletRequest request) {

		String token = request.getHeader("Authorization").substring(7);

		String role = jwtUtil.extractRole(token);
		String dealerCode = jwtUtil.extractDealerCode(token);

		return ResponseEntity.ok(ApiResponse.success(orderService.getAll(role, dealerCode)));
	}
//-------------------------------------------------------------------------------------------

	@PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Orders>> getById(@PathVariable Long id) {
		Orders order = orderService.getById(id);
		return ResponseEntity.ok(ApiResponse.success(order));
	}

//------------------------------------------------------------------------------------------
	@PreAuthorize("hasAnyRole('Admin')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
		orderService.delete(id);
		return ResponseEntity.ok(ApiResponse.success("Order deleted successfully", null));
	}

	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	@GetMapping("/count")
	public ResponseEntity<ApiResponse<Long>> getOrderCount(HttpServletRequest request) {

		String token = request.getHeader("Authorization").substring(7);

		String role = jwtUtil.extractRole(token);
		String dealerCode = jwtUtil.extractDealerCode(token);

		long count = orderService.getOrderCount(role, dealerCode);

		return ResponseEntity.ok(ApiResponse.success("Order count fetched", count));
	}

//----------------------------------------------------------------------------------------
	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	@PutMapping("/cancel/{id}")
	public ResponseEntity<ApiResponse<Orders>> cancelOrder(@PathVariable Long id) {

		Orders cancelled = orderService.cancelOrder(id);

		return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully", cancelled));
	}

	// ----------------------------------------------------------------------------
	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	@GetMapping("/cancelled/count")
	public ResponseEntity<ApiResponse<Long>> cancelledCount() {
		long count = orderService.getCancelledOrderCount();
		return ResponseEntity.ok(ApiResponse.success("Cancelled order count fetched", count));
	}

	// --------------------------------------------------------------------
	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	@GetMapping("/cancelled")
	public ResponseEntity<ApiResponse<List<Orders>>> getAllCancelledOrders() {
		return ResponseEntity
				.ok(ApiResponse.success("Cancelled orders fetched successfully", orderService.getAllCancelledOrders()));
	}

	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	@PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse<Orders>> updateOrder(@PathVariable Long id, @RequestPart("order") Orders order,
			@RequestPart(value = "enquiryForm", required = false) MultipartFile enquiryForm,
			@RequestPart(value = "finalizationReport", required = false) MultipartFile finalizationReport,
			@RequestPart(value = "actualDimension", required = false) MultipartFile actualDimension,
			@RequestPart(value = "design2D3D", required = false) MultipartFile design2D3D) throws Exception {

		Orders updated = orderService.updateOrderWithFiles(id, order, enquiryForm, finalizationReport, actualDimension,
				design2D3D);

		return ResponseEntity.ok(ApiResponse.success("Order updated successfully", updated));
	}

	@PutMapping("/send-to-admin/{id}")
	@PreAuthorize("hasRole('Dealer')")
	public ResponseEntity<ApiResponse<Orders>> sendToAdmin(@PathVariable Long  id) {

	    Orders order = orderService.updateWorkflowStatus(
	            id, OrderWorkflowStatus.SENT_TO_ADMIN);

	    return ResponseEntity.ok(ApiResponse.success("Sent to Admin", order));
	}

	@PutMapping("/send-to-dealer/{id}")
	@PreAuthorize("hasAnyRole('Admin','Employee')")
	public ResponseEntity<ApiResponse<Orders>> sendToDealer(@PathVariable Long  id) {

	    Orders order = orderService.updateWorkflowStatus(
	            id, OrderWorkflowStatus.SENT_TO_DEALER);

	    return ResponseEntity.ok(ApiResponse.success("Sent to Dealer", order));
	}
	
	@PostMapping("/add-remark/{id}")
	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	public ResponseEntity<ApiResponse<Orders>> addRemark(
	        @PathVariable Long id,
	        @RequestParam String remark,
	        @RequestHeader("Authorization") String token) {

	    String jwt = token.substring(7);

	    String username = jwtUtil.extractUsername(jwt);
	    String role = jwtUtil.extractRole(jwt);

	    Orders order = orderService.addRemark(id, remark, username, role);

	    return ResponseEntity.ok(ApiResponse.success("Remark added", order));
	}
	

	@PutMapping("/dealer-remark/{id}")
	@PreAuthorize("hasRole('Dealer')")
	public ResponseEntity<ApiResponse<Orders>> dealerRemark(
	        @PathVariable Long  id,
	        @RequestParam String remark,
	        @RequestHeader("Authorization") String token) {

	    String jwt = token.substring(7);
	    String username = jwtUtil.extractUsername(jwt);

	    Orders order = orderService.addDealerRemark(id, remark, username);

	    return ResponseEntity.ok(ApiResponse.success("Remark added", order));
	}
	
	@GetMapping("/remarks/{orderId}")
	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	public ResponseEntity<ApiResponse<List<OrderRemark>>> getRemarks(@PathVariable Long orderId) {

	    List<OrderRemark> remarks =
	            orderService.getRemarksByOrderId(orderId);

	    return ResponseEntity.ok(ApiResponse.success("Remarks fetched", remarks));
	}
	
	@PutMapping("/dealer-approve/{id}")
	@PreAuthorize("hasRole('Dealer')")
	public ResponseEntity<ApiResponse<Orders>> dealerApprove(@PathVariable Long id) {
		Orders order = orderService.updateWorkflowStatus(id, OrderWorkflowStatus.DEALER_APPROVED);
		return ResponseEntity.ok(ApiResponse.success("Approved", order));
	}

	@PutMapping("/send-to-production/{id}")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<ApiResponse<Orders>> sendToProduction(@PathVariable Long  id) {

		Orders order = orderService.sendToProduction(id);
		return ResponseEntity.ok(ApiResponse.success("Sent to Production", order));
	}
	
	@GetMapping("/production")
	@PreAuthorize("hasAnyRole('Admin','Employee')")
	public ResponseEntity<ApiResponse<List<Orders>>> getProductionOrders() {

	    List<Orders> orders =
	            orderService.getOrdersByWorkflowStatus(
	                    OrderWorkflowStatus.READY_FOR_PRODUCTION);

	    return ResponseEntity.ok(
	            ApiResponse.success("Production Orders", orders));
	}
	//--------------------------------------------------------------
	@GetMapping("/by-workflow/{status}")
	@PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
	public ResponseEntity<ApiResponse<List<Orders>>> getByWorkflowStatus(
	        @PathVariable String status) {

	    List<Orders> orders = orderService.getByWorkflowStatus(status);

	    return ResponseEntity.ok(
	            ApiResponse.success("Orders fetched", orders));
	}
	
	@GetMapping("/by-status/{status}")
	public ResponseEntity<ApiResponse<List<Orders>>> getByOrderStatus(@PathVariable String status) {

	    List<Orders> orders = orderService.getByOrderStatus(status);

	    return ResponseEntity.ok(
	            ApiResponse.success("Orders fetched", orders));
	}
	
	@GetMapping("/search-by-date")
	@PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
	public ResponseEntity<ApiResponse<List<Orders>>> searchByDate(
	        HttpServletRequest request,
	        @RequestParam String fromDate,
	        @RequestParam String toDate) {

	    String token = request.getHeader("Authorization").substring(7);

	    String role = jwtUtil.extractRole(token);
	    String dealerCode = jwtUtil.extractDealerCode(token);

	    LocalDate from = LocalDate.parse(fromDate);
	    LocalDate to = LocalDate.parse(toDate);

	    List<Orders> orders = orderService.getOrdersByDateRange(role, dealerCode, from, to);

	    return ResponseEntity.ok(ApiResponse.success("Orders fetched", orders));
	}
	
	@GetMapping("/completed/search-by-date")
	@PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
	public ResponseEntity<ApiResponse<List<Orders>>> getCompletedByDate(
	        @RequestParam String fromDate,
	        @RequestParam String toDate) {

	    LocalDate from = LocalDate.parse(fromDate);
	    LocalDate to = LocalDate.parse(toDate);

	    List<Orders> orders = orderService.getCompletedOrdersByDate(from, to);

	    return ResponseEntity.ok(ApiResponse.success("Completed Orders fetched", orders));
	}
	@GetMapping("/cancelled/search-by-date")
	@PreAuthorize("hasAnyRole('Admin','Dealer')")
	public ResponseEntity<ApiResponse<List<Orders>>> searchCancelledByDate(
	        @RequestParam String fromDate,
	        @RequestParam String toDate) {

	    LocalDate from = LocalDate.parse(fromDate);
	    LocalDate to = LocalDate.parse(toDate);

	    List<Orders> orders = orderService.getCancelledOrdersByDate(from, to);

	    return ResponseEntity.ok(
	            ApiResponse.success("Cancelled orders by date", orders));
	}
	@GetMapping("/report/search-by-date")
	@PreAuthorize("hasAnyRole('Admin','Dealer','Employee')")
	public ResponseEntity<ApiResponse<List<Map<String, Object>>>> searchByDateWithDealer(
	        HttpServletRequest request,
	        @RequestParam String fromDate,
	        @RequestParam String toDate) {

	    String token = request.getHeader("Authorization").substring(7);

	    String role = jwtUtil.extractRole(token);
	    String dealerCode = jwtUtil.extractDealerCode(token);

	    List<Map<String, Object>> list =
	            orderService.getOrdersWithDealerName(
	                    role,
	                    dealerCode,
	                    LocalDate.parse(fromDate),
	                    LocalDate.parse(toDate)
	            );

	    return ResponseEntity.ok(ApiResponse.success("Filtered orders", list));
	}
}