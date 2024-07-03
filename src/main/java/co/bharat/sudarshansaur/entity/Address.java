package co.bharat.sudarshansaur.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
	
	@Column(length = 10)
	private String houseNo;

	private String area;

	private String street1;

	private String street2;
	
	@Column(length = 40)
	private String landmark;
	
	@Column(length = 40)
	private String town;
	
	@Column(length = 40)
	private String taluk;
	
	@Column(length = 40)
	private String district;
	
	@Column(length = 40)
	private String state;

	@Column(length = 40)
	private String country = "India";

	@Column(length = 6)
	private String zipCode;

	@Override
	public String toString() {
		return houseNo + ", " +
				area + ", " +
				street1 + ", " +
				(street2.isEmpty() ? "" : street2 + ", ") +
				(landmark.isEmpty() ? "" : "Near " + landmark + ", ") +
				town + ", " +
				taluk + ", " +
				district + ", " +
				state + ", " +
				zipCode;
	}
}
