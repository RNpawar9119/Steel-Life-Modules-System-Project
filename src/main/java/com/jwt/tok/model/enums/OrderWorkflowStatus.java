package com.jwt.tok.model.enums;

public enum OrderWorkflowStatus {

	DRAFT, // dealer created order
	SENT_TO_ADMIN, // dealer - Admin
	SENT_TO_DEALER, // admin - Dealer
	DEALER_REMARKED, // Dealer added remark
	DEALER_APPROVED, // Dealer approved
	READY_FOR_PRODUCTION, // Final stage
	COMPLETED
}