package co.bharat.sudarshansaur.entity;


import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
public class Stockists {
	
	@Id
	@GeneratedValue
	private long stockistId;

	private String stockistName;
	
	@JsonIgnore
	private String password;

	private String mobileNo;

	private UserStatus status;
	
	private String email;

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
	
	private String businessName;
	
	private String gstNumber;
	
	@OneToMany(mappedBy = "stockists", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<Dealers> dealers;
	
}
