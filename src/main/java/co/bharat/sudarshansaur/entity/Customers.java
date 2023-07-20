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

import com.fasterxml.jackson.annotation.JsonManagedReference;

import co.bharat.sudarshansaur.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customers {

	@Id
	@GeneratedValue
	private long customerId;

	private String customerName;

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

	private Date lastPurchaseDate;

	// @OneToMany(mappedBy = "customers", cascade = CascadeType.ALL, orphanRemoval =
	// true)

	//@OneToMany(mappedBy = "customers")
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<WarrantyDetails> warrantyDetails;

}
