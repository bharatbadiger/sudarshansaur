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
        @AttributeOverride(name = "houseNo", column = @Column(name = "inst_add_houseNo", length = 10)),
        @AttributeOverride(name = "area", column = @Column(name = "inst_add_area")),
        @AttributeOverride(name = "street1", column = @Column(name = "inst_add_street1")),
        @AttributeOverride(name = "street2", column = @Column(name = "inst_add_street2")),
        @AttributeOverride(name = "landmark", column = @Column(name = "inst_add_landmark", length = 30)),
        //@AttributeOverride(name = "mobile2", column = @Column(name = "inst_add_mobile2", length = 14)),
        @AttributeOverride(name = "town", column = @Column(name = "inst_add_town", length = 20)),
        @AttributeOverride(name = "taluk", column = @Column(name = "inst_add_taluk", length = 20)),
        @AttributeOverride(name = "state", column = @Column(name = "inst_add_state", length = 20)),
        @AttributeOverride(name = "country", column = @Column(name = "inst_add_country", length = 20)),
        @AttributeOverride(name = "zipCode", column = @Column(name = "inst_add_zipCode", length = 6))
    })
    private Address installationAddress;
    
    @Embedded
    private Address ownerAddress;
    
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="warranty_serial_no")
	private WarrantyDetails warrantyDetails;
		
	@Embedded
	@AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "dealer_name", length = 40)),
        @AttributeOverride(name = "mobile", column = @Column(name = "dealer_mobile", length = 14)),
        @AttributeOverride(name = "place", column = @Column(name = "dealer_place", length = 30))
	})
	private UserInfo dealerInfo;
	
	@Embedded
	@AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "technician_name", length = 40)),
        @AttributeOverride(name = "mobile", column = @Column(name = "technician_mobile", length = 14)),
        @AttributeOverride(name = "place", column = @Column(name = "technician_place", length = 30))
	})
	private UserInfo technicianInfo;
	
	@Embedded
	@AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "plumber_name", length = 40)),
        @AttributeOverride(name = "mobile", column = @Column(name = "plumber_mobile", length = 14)),
        @AttributeOverride(name = "place", column = @Column(name = "plumber_place", length = 30))
	})
	private UserInfo plumberInfo;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Answers> answers; 
	
	@Column(length = 20)
	private AllocationStatus status;
	
	@Column(length = 50)
	private String rejectReason;
	
	@Column(length =14)
	private String mobile2;
	
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
	
	@Column(length = 40)
	private String initiatedBy;

	@Column(length = 40)
	private String approvedBy;

}
