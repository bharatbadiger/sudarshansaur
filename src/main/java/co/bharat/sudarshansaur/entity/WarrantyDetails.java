package co.bharat.sudarshansaur.entity;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	
	private String dealers;
	
	private String state;
	
	private String description;
	
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customers customers;
	
	//To Add issued date & dealer issued date
	
	//Unassign warranty only when no customer is assigned
	//System
	
	

}
