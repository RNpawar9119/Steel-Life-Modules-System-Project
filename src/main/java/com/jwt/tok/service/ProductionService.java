package com.jwt.tok.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jwt.tok.model.Employee;
import com.jwt.tok.model.Login;
import com.jwt.tok.model.OrderItem;
import com.jwt.tok.model.OrderItemProgress;
import com.jwt.tok.model.Orders;
import com.jwt.tok.model.SequenceProcess;
import com.jwt.tok.model.enums.OrderWorkflowStatus;
import com.jwt.tok.repository.EmployeeRepository;
import com.jwt.tok.repository.LoginRepository;
import com.jwt.tok.repository.OrderItemProgressRepository;
import com.jwt.tok.repository.OrderItemRepository;
import com.jwt.tok.repository.OrderRepository;
import com.jwt.tok.repository.SequenceProcessRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductionService {

	private final OrderItemRepository itemRepo;
	private final OrderItemProgressRepository progressRepo;
	private final OrderRepository orderRepo;
	private final SequenceProcessRepository sequenceProcessRepo;
	private final LoginRepository loginRepo;
	private final EmployeeRepository employeeRepository;

	public OrderItemProgress startProcess(Long itemId, Long processId) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Login login = loginRepo.findByUsername(username);
		String role = login.getRole().getRole();

		List<OrderItemProgress> history = progressRepo.findByOrderItemIdAndSequenceProcessIdOrderByIdDesc(itemId,
				processId);

		OrderItemProgress lastRecord = history.isEmpty() ? null : history.get(0);

		// 🔴 If already running
		if (lastRecord != null && "RUNNING".equals(lastRecord.getStatus())) {

			if ("Admin".equals(role)) {
				return lastRecord;
			}

			Employee currentEmp = employeeRepository.findByUsername(username)
					.orElseThrow(() -> new RuntimeException("Employee not found"));

			if (lastRecord.getStartedBy() != null && !lastRecord.getStartedBy().getId().equals(currentEmp.getId())) {

				throw new RuntimeException("Process already running by another employee");
			}

			return lastRecord;
		}

		// 🔴 Always create NEW row
		OrderItem item = itemRepo.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));

		SequenceProcess process = sequenceProcessRepo.findById(processId)
				.orElseThrow(() -> new RuntimeException("Process not found"));

		OrderItemProgress progress = new OrderItemProgress();
		progress.setOrderItem(item);
		progress.setSequenceProcess(process);
		progress.setStatus("RUNNING");
		progress.setStartTime(LocalDateTime.now());

		if ("Employee".equals(role)) {
			Employee emp = employeeRepository.findByUsername(username)
					.orElseThrow(() -> new RuntimeException("Employee not found"));
			progress.setStartedBy(emp);
		}

		return progressRepo.save(progress);
	}

	// --------------------------------------------------------------

	public OrderItemProgress stopProcess(Long itemId, Long processId) {

	    String username = SecurityContextHolder.getContext().getAuthentication().getName();

	    Login login = loginRepo.findByUsername(username);
	    String role = login.getRole().getRole();

	    List<OrderItemProgress> history =
	            progressRepo.findByOrderItemIdAndSequenceProcessIdOrderByIdDesc(itemId, processId);

	    OrderItemProgress running = history.stream()
	            .filter(p -> "RUNNING".equals(p.getStatus()))
	            .findFirst()
	            .orElseThrow(() -> new RuntimeException("No running process found"));

	    // ✅ ADMIN FULL ACCESS
	    if ("Admin".equals(role)) {

	        running.setStatus("STOPPED");
	        running.setStopTime(LocalDateTime.now());

	        return progressRepo.save(running);
	    }

	    // 🔴 ADMIN STARTED PROCESS → EMPLOYEE BLOCKED
	    if (running.getStartedBy() == null) {
	        throw new RuntimeException("This process was started by Admin. Only Admin can stop it.");
	    }

	    // 🔴 DIFFERENT EMPLOYEE BLOCKED
	    if (!running.getStartedBy().getUsername().equals(username)) {
	        throw new RuntimeException("You cannot stop another employee's process.");
	    }

	    // ✅ SAME EMPLOYEE
	    running.setStatus("STOPPED");
	    running.setStopTime(LocalDateTime.now());

	    return progressRepo.save(running);
	}
	// ================= COMPLETE =================

	public OrderItemProgress completeProcess(Long itemId, Long processId) {

	    String username = SecurityContextHolder.getContext().getAuthentication().getName();

	    Login login = loginRepo.findByUsername(username);
	    String role = login.getRole().getRole();

	    List<OrderItemProgress> history =
	            progressRepo.findByOrderItemIdAndSequenceProcessIdOrderByIdDesc(itemId, processId);

	    OrderItemProgress running = history.stream()
	            .filter(p -> "RUNNING".equals(p.getStatus()))
	            .findFirst()
	            .orElseThrow(() -> new RuntimeException("No running process found"));

	    // ✅ ADMIN FULL ACCESS
	    if ("Admin".equals(role)) {

	        running.setStatus("COMPLETED");
	        running.setCompletedTime(LocalDateTime.now());

	        progressRepo.save(running);

	        checkOrderCompletion(running.getOrderItem());

	        return running;
	    }

	    // 🔴 ADMIN STARTED PROCESS
	    if (running.getStartedBy() == null) {
	        throw new RuntimeException("This process was started by Admin. Only Admin can complete it.");
	    }

	    // 🔴 DIFFERENT EMPLOYEE
	    if (!running.getStartedBy().getUsername().equals(username)) {
	        throw new RuntimeException("You cannot complete another employee's process.");
	    }

	    // ✅ SAME EMPLOYEE
	    running.setStatus("COMPLETED");
	    running.setCompletedTime(LocalDateTime.now());

	    progressRepo.save(running);

	    checkOrderCompletion(running.getOrderItem());

	    return running;
	}
	// ================= ORDER AUTO COMPLETE =================

	private void checkOrderCompletion(OrderItem item) {

		if (item.getSequence() == null)
			throw new RuntimeException("Sequence not assigned to this OrderItem");

		int totalProcesses = item.getSequence().getProcesses().size();

		long completedCount = progressRepo.countByOrderItemIdAndStatus(item.getId(), "COMPLETED");

		if (totalProcesses == completedCount && totalProcesses > 0) {

			item.setStatus("COMPLETED");
			itemRepo.save(item);

		} else {

			item.setStatus("IN_PROGRESS");
			itemRepo.save(item);
			return;
		}

		Orders order = item.getOrder();

		boolean allItemsCompleted = order.getItems().stream().allMatch(i -> "COMPLETED".equals(i.getStatus()));

		if (allItemsCompleted) {

			order.setStatus("COMPLETED");
			order.setWorkflowStatus(OrderWorkflowStatus.COMPLETED);

			orderRepo.save(order);
		}
	}

//	public List<OrderItemProgress> getItemProgress(Long itemId) {
//		return progressRepo.findByOrderItemId(itemId);
//	}

//	public List<Map<String, Object>> getItemProgress(Long itemId) {
//
//		List<OrderItemProgress> list = progressRepo.findByOrderItemId(itemId);
//
//		Map<Long, OrderItemProgress> latestMap = new HashMap<>();
//
//		for (OrderItemProgress p : list) {
//
//			Long processId = p.getSequenceProcess().getId();
//
//			if (!latestMap.containsKey(processId) || p.getId() > latestMap.get(processId).getId()) {
//
//				latestMap.put(processId, p);
//			}
//		}
//
//		return latestMap.values().stream().map(p -> {
//			Map<String, Object> map = new HashMap<>();
//			map.put("processId", p.getSequenceProcess().getId());
//			map.put("status", p.getStatus());
//			map.put("startedById", p.getStartedBy() != null ? p.getStartedBy().getId() : null);
//			return map;
//		}).collect(Collectors.toList());
//	}
	
	public List<Map<String, Object>> getItemProgress(Long itemId) {

	    List<OrderItemProgress> list = progressRepo.findByOrderItemId(itemId);

	    Map<Long, OrderItemProgress> latestMap = new HashMap<>();

	    for (OrderItemProgress p : list) {

	        Long processId = p.getSequenceProcess().getId();

	        if (!latestMap.containsKey(processId) || p.getId() > latestMap.get(processId).getId()) {
	            latestMap.put(processId, p);
	        }
	    }

	    return latestMap.values().stream().map(p -> {
	        Map<String, Object> map = new HashMap<>();

	        map.put("processId", p.getSequenceProcess().getId());
	        map.put("status", p.getStatus());

	        // ✅ EMPLOYEE NAME LOGIC
	        if (p.getStartedBy() != null) {
	            map.put("employeeName", p.getStartedBy().getEmployeeName());
	        } else {
	            map.put("employeeName", "Admin");
	        }

	        return map;

	    }).collect(Collectors.toList());
	}

	public List<Orders> getRunningProductionOrders() {

		List<Orders> allOrders = orderRepo.findAll();

		return allOrders.stream()
				.filter(order -> order.getItems() != null && order.getItems().stream().anyMatch(item -> {

					List<OrderItemProgress> progressList = progressRepo.findByOrderItemId(item.getId());

					if (progressList.isEmpty())
						return true;

					return progressList.stream().anyMatch(p -> !"COMPLETED".equals(p.getStatus()));
				})).collect(Collectors.toList());
	}

//	public List<Orders> getCompletedProductionOrders() {
//
//		List<Orders> allOrders = orderRepo.findAll();
//
//		return allOrders.stream()
//				.filter(order -> order.getItems() != null && order.getItems().stream().allMatch(item -> {
//
//					List<OrderItemProgress> progressList = progressRepo.findByOrderItemId(item.getId());
//
//					if (progressList.isEmpty())
//						return false;
//
//					return progressList.stream().allMatch(p -> "COMPLETED".equals(p.getStatus()));
//				})).collect(Collectors.toList());
//	}

	public List<Orders> getCompletedProductionOrders() {

	    List<Orders> allOrders = orderRepo.findAll();

	    return allOrders.stream()
	        .filter(order -> order.getItems() != null && 
	            order.getItems().stream().allMatch(item -> {

	                if (item.getSequence() == null)
	                    return false;

	                List<SequenceProcess> processes = item.getSequence().getProcesses();

	                for (SequenceProcess process : processes) {

	                    List<OrderItemProgress> history =
	                        progressRepo.findByOrderItemIdAndSequenceProcessIdOrderByIdDesc(
	                            item.getId(),
	                            process.getId()
	                        );

	                    if (history.isEmpty())
	                        return false;

	                    OrderItemProgress latest = history.get(0);

	                    if (!"COMPLETED".equals(latest.getStatus()))
	                        return false;
	                }

	                return true;
	            })
	        )
	        .collect(Collectors.toList());
	}
	
	public List<Map<String, Object>> getProcessHistory(Long itemId, Long processId) {

		List<OrderItemProgress> history = progressRepo.findByOrderItemIdAndSequenceProcessIdOrderByIdDesc(itemId,
				processId);

		return history.stream().map(p -> {

			Map<String, Object> map = new HashMap<>();

			//map.put("employeeName", p.getStartedBy() != null ? p.getStartedBy().getUsername() : "Admin");

			
			 if (p.getStartedBy() != null) {
		            map.put("employeeUsername", p.getStartedBy().getUsername());
		            map.put("employeeName", p.getStartedBy().getEmployeeName());
		            map.put("employeeCode", p.getStartedBy().getEmployeeCode());
		        } else {
		            map.put("employeeUsername", "Admin");
		            map.put("employeeName", "Admin");
		            map.put("employeeCode", "-");
		        }
			 
			map.put("startTime", p.getStartTime());
			map.put("stopTime", p.getStopTime());
			map.put("completedTime", p.getCompletedTime());
			map.put("status", p.getStatus());

			return map;

		}).collect(Collectors.toList());
	}
	
	public List<Orders> getRunningOrdersForAdmin() {

	    List<Orders> allOrders = orderRepo.findAll();

	    return allOrders.stream()
	        .map(order -> {

	            List<OrderItem> filteredItems = order.getItems().stream()
	                .filter(item -> {

	                    List<OrderItemProgress> history =
	                        progressRepo.findByOrderItemId(item.getId());

	                    if (history.isEmpty()) return false;

	                    Map<Long, OrderItemProgress> latestMap = new HashMap<>();

	                    for (OrderItemProgress p : history) {
	                    	Long processId = p.getSequenceProcess().getId();

	                        if (!latestMap.containsKey(processId)
	                                || p.getId() > latestMap.get(processId).getId()) {

	                            latestMap.put(processId, p);
	                        }
	                    }

	                    return latestMap.values().stream()
	                        .anyMatch(p -> !"COMPLETED".equals(p.getStatus()));
	                })
	                .collect(Collectors.toList());

	            order.setItems(filteredItems); // 🔥 IMPORTANT

	            return order;

	        })
	        .filter(order -> !order.getItems().isEmpty()) // empty orders remove
	        .collect(Collectors.toList());
	}
	
}