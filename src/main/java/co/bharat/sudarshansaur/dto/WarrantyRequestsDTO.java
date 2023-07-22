package co.bharat.sudarshansaur.dto;


import java.util.Date;

import co.bharat.sudarshansaur.entity.Customers;
import co.bharat.sudarshansaur.entity.Dealers;
import co.bharat.sudarshansaur.entity.WarrantyDetails;
import co.bharat.sudarshansaur.enums.AllocationStatus;
import co.bharat.sudarshansaur.enums.UserType;
import co.bharat.sudarshansaur.interfaces.Users;
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
	
	private WarrantyDetails warrantyDetails;

	private AllocationStatus allocationStatus;
	
	private Dealers dealers;
	
	private Customers customers;
	
    private Date createdOn;

	private Date updatedOn;
	
	private UserType initUserType;
	
	private Users initiatedBy;

	private String approvedBy;

}
