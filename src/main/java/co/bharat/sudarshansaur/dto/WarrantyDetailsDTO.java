package co.bharat.sudarshansaur.dto;


import java.util.Date;

import co.bharat.sudarshansaur.enums.AllocationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarrantyDetailsDTO {
	
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
	
	private Long customer;
	
}
