package co.bharat.sudarshansaur.entity;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import co.bharat.sudarshansaur.enums.AllocationStatus;
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

	private String invoiceNo;

	private String itemDescription;
	
	private String model;
	
	private String LPD;

	private Date installationDate;
	
	private String guranteePeriod;
	
	private Date validTill;
	
	private Date custBillDate;
	
	private AllocationStatus allocationStatus;
	
	private String billNo;
	
	private String subDealer;
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dealer_id")
    @JsonBackReference("dealers-warranty")
	private Dealers dealers;
	
	private String state;
	
	private String description;
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    @JsonBackReference("customer-warranty")
	private Customers customer;
	
	//To Add issued date & dealer issued date
	
	//Unassign warranty only when no customer is assigned
	//System
	
	

}
