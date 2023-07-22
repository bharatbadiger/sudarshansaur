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
	private String addressLine1;

	private String addressLine2;

	private String city;

	private String state;

	private String country = "India";

	private String zipCode;

}
