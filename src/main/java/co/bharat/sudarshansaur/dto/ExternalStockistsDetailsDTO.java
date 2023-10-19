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
public class ExternalStockistsDetailsDTO {
	
	@JsonProperty("Dealer Code")
	private String dealerCode;
	
	@JsonProperty("Products Deals in")
	private String productsDealsIn;	
	
	@JsonProperty("Dealer Firm Name")
	private String dealerFirmName;

	@JsonProperty("Contact Person")
	private String contactPerson;
	
	@JsonProperty("Sales Executive")
	private String salesExecutive;

	@JsonProperty("Applicable To Lead")
	private Date applicableToLead;
	
	@JsonProperty("Active")
	private String active;
	
	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("Assigned to")
	private String assignedTo;
	
	@JsonProperty("Description")
	private String description;
	
	@JsonProperty("Dealers Email")
	private Date delaersEmail;	
	
	@JsonProperty("State")
	private String state;
	
	@JsonProperty("district")
	private String district;
	
	@JsonProperty("Taluka")
	private String taluka;
	
	@JsonProperty("Postal Code")
	private String postalCode;
	
	@JsonProperty("Marketing Area")
	private String marketingArea;
	
	@JsonProperty("Address")
	private String address;
	
}
