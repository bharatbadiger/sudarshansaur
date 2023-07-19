package co.bharat.sudarshansaur.entity;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import co.bharat.sudarshansaur.enums.UserStatus;
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
public class Dealers {
	
	@Id
	@GeneratedValue
	private long dealerId;

	private String dealerName;
	
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
	
	private String stockistId;

}
