package co.bharat.sudarshansaur.dto;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalWarrantyDetailsDTO {
	
	@JsonProperty("System Serial No")
	private String warrantySerialNo;
	
	@JsonProperty("Customers")
	private String crmCustomerName;	
	
	@JsonProperty("Customers Phone No.")
	private String crmCustomerMobileNo;

	@JsonProperty("Item Description")
	private String itemDescription;
	
	@JsonProperty("LPD")
	private String LPD;

	@JsonProperty("Installation Date")
	private Date installationDate;
	
	@JsonProperty("Model")
	private String model;
	
	@JsonProperty("Guarantee Status")
	private String guaranteeStatus;
	
	@JsonProperty("Guarantee Period")
	private String guaranteePeriod;
	
	@JsonProperty("Assigned to")
	private String assignedTo;	
	
	@JsonProperty("Cust. Bill Date")
	private Date custBillDate;	
	
	@JsonProperty("Bill No")
	private String billNo;

	@JsonProperty("Invoice Number")
	private String invoiceNo;
	
	@JsonProperty("Sub Dealer")
	private String crmDealerName;
	
	@JsonProperty("Dealers")
	private String crmStockistName;
	
	@JsonProperty("Dealers Phone No.")
	private String crmStockistMobileNo;
	
	@JsonProperty("Dealers Email")
	private String crmStockistEmail;
	
	@JsonProperty("State")
	private String state;
	
	@JsonProperty("Description")
	private String description;
	
	@JsonProperty("district")
	private String crmStockistDistrict;
	
}
