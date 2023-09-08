package co.bharat.sudarshansaur.entity;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import co.bharat.sudarshansaur.enums.UserStatus;
import co.bharat.sudarshansaur.interfaces.Users;
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
public class Customers implements Users{

	@Id
	@GeneratedValue
	private long customerId;

	private String customerName;
	
	//private String password;
	
	@Column(unique = true)
	private String mobileNo;

	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@Column(unique = true)
	private String email;

	@Embedded
	private Address address;
	
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

}
