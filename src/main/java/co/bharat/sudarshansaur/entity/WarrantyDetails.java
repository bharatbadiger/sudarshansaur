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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
	
	private String crmStockistDistrict;

	private String stockists;
	private String dealers;
	private String customer;

}
