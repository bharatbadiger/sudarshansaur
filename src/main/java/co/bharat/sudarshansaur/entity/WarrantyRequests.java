package co.bharat.sudarshansaur.entity;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name = "WarrantyRequests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarrantyRequests {
	
	@Id
	@GeneratedValue
	private long requestId;
	
	@OneToOne
	@JoinColumn(name="warranty_serial_no")
	private WarrantyDetails warrantyDetails;

	private AllocationStatus allocationStatus;
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dealer_id")
	private Dealers dealers;
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
	private Customers customers;
	
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
	
	private UserType initUserType;
	
	private String initiatedBy;

	private String approvedBy;

}
