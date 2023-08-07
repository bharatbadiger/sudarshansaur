package co.bharat.sudarshansaur.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import co.bharat.sudarshansaur.enums.UserStatus;
import co.bharat.sudarshansaur.interfaces.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Dealers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dealers implements Users {
	
	@Id
	@GeneratedValue
	private long dealerId;

	private String dealerName;

	private String password;
	
	@Column(unique = true)
	private String mobileNo;

	@Enumerated(EnumType.STRING)
	private UserStatus status;
	
	@Column(unique = true)
	private String email;
	
	@Embedded
	private Address address;
	
	private Date createdOn;
    @PrePersist
    protected void onCreate() {
    	createdOn = new Date();
    	updatedOn = createdOn;
    }
	
	private Date updatedOn;
    @PreUpdate
    protected void onUpdate() {
    	updatedOn = new Date();
    }
	
	private Date lastLogin;
	
	private String image;
	
	private String businessAddress;
	
	private String businessName;
	
	private String gstNumber;
	
	@ManyToOne
	@JoinColumn(name="stockist_id")
	@JsonBackReference("dealers-stockists")
	private Stockists stockists;
	
	/*
	 * @OneToMany(mappedBy = "dealers", cascade = CascadeType.ALL, fetch =
	 * FetchType.LAZY)
	 * 
	 * @JsonManagedReference("dealers-warranty") private List<WarrantyDetails>
	 * warrantyDetails;
	 */

}
