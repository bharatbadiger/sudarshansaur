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
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stockist_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "stockistId")
    @JsonIdentityReference(alwaysAsId = true)
	private Stockists stockists;
    
    // Getter method to return stockists as a String
    @JsonProperty("stockists")
    public String getStockistsAsString() {
        if (stockists != null) {
            return String.valueOf(stockists.getStockistId());
        }
        return null; // Or an appropriate default value if stockists is null
    }
    
	/*
	 * @JsonValue public String getStockistIdAsString() { if (stockists != null) {
	 * return String.valueOf(stockists.getStockistId()); } return null; // Or an
	 * appropriate default value if stockists is null }
	 */
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dealer_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dealerId")
    @JsonIdentityReference(alwaysAsId = true)
    //@JsonBackReference("dealers-warranty")
	private Dealers dealers;
	
	/*
	 * @JsonValue public String getDealerIddAsString() { if (dealers != null) {
	 * return String.valueOf(dealers.getDealerId()); } return null; // Or an
	 * appropriate default value if stockists is null }
	 */
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "customerId")
    @JsonIdentityReference(alwaysAsId = true)
    //@JsonBackReference("customer-warranty")
	private Customers customer;
    
	/*
	 * @JsonValue public String getCustomerIdAsString() { if (customer != null) {
	 * return String.valueOf(customer.getCustomerId()); } return null; // Or an
	 * appropriate default value if stockists is null }
	 */
    
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
	
}
