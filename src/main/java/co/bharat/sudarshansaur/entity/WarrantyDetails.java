package co.bharat.sudarshansaur.entity;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import co.bharat.sudarshansaur.enums.AllocationStatus;
import co.bharat.sudarshansaur.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "WarrantyDetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarrantyDetails {
	
	@Id
	private String warrantySerialNo;
	
	private String crmCustomerName;	
	
	private String crmCustomerMobileNo;

	private String itemDescription;
	
	private String LPD;

	private Date installationDate;
	
	private String model;
	
	private String guaranteeStatus;
	
	private String guaranteePeriod;
	
	private String assignedTo;	
	
	private Date custBillDate;	
	
	private String billNo;

	private String invoiceNo;
	
	private String crmDealerName;
	
	private String crmStockistName;
	
	private String crmStockistMobileNo;
	
	private String crmStockistEmail;
	
	private String state;
	
	private String description;
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stockist_id")
	private Stockists stockists;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dealer_id")
    //@JsonBackReference("dealers-warranty")
	private Dealers dealers;
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    //@JsonBackReference("customer-warranty")
	private Customers customer;
    
    @Enumerated(EnumType.STRING)
    private AllocationStatus allocationStatus;
    
    private Date createdOn;

	@PrePersist
	protected void onCreate() {
		createdOn = new Date();
		updatedOn = createdOn;
		approvedBy = initiatedBy;
	}

	private Date updatedOn;

	@PreUpdate
	protected void onUpdate() {
		updatedOn = new Date();
	}
	
	@Enumerated(EnumType.STRING)
	private UserType initUserType;
	
	private String initiatedBy;

	private String approvedBy;
	
}
