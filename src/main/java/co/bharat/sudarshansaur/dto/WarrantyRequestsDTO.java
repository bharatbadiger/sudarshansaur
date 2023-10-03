package co.bharat.sudarshansaur.dto;


import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import co.bharat.sudarshansaur.entity.Address;
import co.bharat.sudarshansaur.entity.Customers;
import co.bharat.sudarshansaur.entity.ImageDetails;
import co.bharat.sudarshansaur.entity.UserInfo;
import co.bharat.sudarshansaur.entity.WarrantyDetails;
import co.bharat.sudarshansaur.enums.AllocationStatus;
import co.bharat.sudarshansaur.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarrantyRequestsDTO {
	
	private long requestId;
	
	private Customers customers;
    
    private Address installationAddress;
    
    private Address ownerAddress;
    
	private WarrantyDetails warrantyDetails;
		
	private UserInfo dealerInfo;
	
	private UserInfo technicianInfo;
	
	private UserInfo plumberInfo;
	
	private List<AnswersDTO> answers; 
	
	private AllocationStatus status;
	
	private String rejectReason;
	
	private String mobile2;
	
	private ImageDetails images; 
	
	private String installationDate;
	
	private String invoiceDate;
	
	private String invoiceNumber;
	
    private Date createdOn;

	private Date updatedOn;

	private UserType initUserType;
	
	private String initiatedBy;

	private String approvedBy;

	private String lat;

	private String lon;
	
	/*
	 * @JsonProperty(value = "isPhotoChecked") private Boolean isPhotoChecked;
	 * 
	 * @JsonProperty(value = "isOtherInfoChecked") private Boolean
	 * isOtherInfoChecked;
	 */
	
	private boolean photoChecked;
	
	private boolean otherInfoChecked;
	
	private String verifiedBy;
	
	private String verifiedDate;

}
