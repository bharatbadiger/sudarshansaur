package co.bharat.sudarshansaur.entity;


import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
	private Customers customers;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "houseNo", column = @Column(name = "inst_add_houseNo")),
        @AttributeOverride(name = "area", column = @Column(name = "inst_add_area")),
        @AttributeOverride(name = "street1", column = @Column(name = "inst_add_street1")),
        @AttributeOverride(name = "street2", column = @Column(name = "inst_add_street2")),
        @AttributeOverride(name = "landmark", column = @Column(name = "inst_add_landmark")),
        @AttributeOverride(name = "mobile2", column = @Column(name = "inst_add_mobile2")),
        @AttributeOverride(name = "town", column = @Column(name = "inst_add_town")),
        @AttributeOverride(name = "taluk", column = @Column(name = "inst_add_taluk")),
        @AttributeOverride(name = "state", column = @Column(name = "inst_add_state")),
        @AttributeOverride(name = "country", column = @Column(name = "inst_add_country")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "inst_add_zipCode"))
    })
    private Address installationAddress;
    
    @Embedded
    private Address ownerAddress;
    
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="warranty_serial_no")
	private WarrantyDetails warrantyDetails;
		
	@Embedded
	@AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "dealer_name")),
        @AttributeOverride(name = "mobile", column = @Column(name = "dealer_mobile")),
        @AttributeOverride(name = "place", column = @Column(name = "dealer_place"))
	})
	private UserInfo dealerInfo;
	
	@Embedded
	@AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "technician_name")),
        @AttributeOverride(name = "mobile", column = @Column(name = "technician_mobile")),
        @AttributeOverride(name = "place", column = @Column(name = "technician_place"))
	})
	private UserInfo technicianInfo;
	
	@Embedded
	@AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "plumber_name")),
        @AttributeOverride(name = "mobile", column = @Column(name = "plumber_mobile")),
        @AttributeOverride(name = "place", column = @Column(name = "plumber_place"))
	})
	private UserInfo plumberInfo;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Answers> answers; 
	
	private AllocationStatus status;
	
	private String rejectReason;
	
	@Embedded
	private ImageDetails images; 
	
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
