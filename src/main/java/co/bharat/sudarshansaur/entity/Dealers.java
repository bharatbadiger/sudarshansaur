package co.bharat.sudarshansaur.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

	@JsonIgnore
	private String password;

	@Column(unique = true)
	private String mobileNo;

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
	
	/*
	 * @Embedded
	 * 
	 * @AttributeOverrides({
	 * 
	 * @AttributeOverride(name = "addressLine1", column = @Column(name =
	 * "business_addressLine1")),
	 * 
	 * @AttributeOverride(name = "addressLine2", column = @Column(name =
	 * "business_addressLine2")),
	 * 
	 * @AttributeOverride(name = "city", column = @Column(name = "business_city")),
	 * 
	 * @AttributeOverride(name = "state", column = @Column(name =
	 * "business_state")),
	 * 
	 * @AttributeOverride(name = "country", column = @Column(name =
	 * "business_country")),
	 * 
	 * @AttributeOverride(name = "zipCode", column = @Column(name =
	 * "business_zipCode")), })
	 */
	private String businessAddress;
	
	private String businessName;
	
	private String gstNumber;
	
	@ManyToOne
	@JoinColumn(name="stockist_id")
	@JsonBackReference
	private Stockists stockists;

}
