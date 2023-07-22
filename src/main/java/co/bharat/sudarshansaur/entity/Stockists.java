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
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import co.bharat.sudarshansaur.enums.UserStatus;
import co.bharat.sudarshansaur.interfaces.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Stockists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stockists implements Users {
	
	@Id
	@GeneratedValue
	private long stockistId;

	private String stockistName;
	
	@JsonIgnore
	private String password;

	private String mobileNo;

	private UserStatus status;
	
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
	
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "addressLine1", column = @Column(name = "business_addressLine1")),
        @AttributeOverride(name = "addressLine2", column = @Column(name = "business_addressLine2")),
        @AttributeOverride(name = "city", column = @Column(name = "business_city")),
        @AttributeOverride(name = "state", column = @Column(name = "business_state")),
        @AttributeOverride(name = "country", column = @Column(name = "business_country")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "business_zipCode")),
    })
	private Address businessAddress;
	
	private String businessName;
	
	private String gstNumber;
	
	@OneToMany(mappedBy = "stockists", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<Dealers> dealers;
	
}
