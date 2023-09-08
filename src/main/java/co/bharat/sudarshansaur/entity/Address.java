package co.bharat.sudarshansaur.entity;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
	
	private String houseNo;

	private String area;

	private String street1;

	private String street2;
	
	private String landmark;
	
	private String mobile2;
	
	private String town;
	
	private String taluk;
	
	private String state;

	private String country = "India";

	private String zipCode;

}
