package co.bharat.sudarshansaur.entity;


import java.util.Date;

import javax.persistence.*;

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

	private String businessName;
	
	private String gstNumber;

	private String stockistCode;

	private String stockistBusinessName;
//
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "stockist_id")
//	private Stockists stockist;


}
