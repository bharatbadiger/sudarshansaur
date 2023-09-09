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
	
	@Column(length = 30)
	private String landmark;
	
	/*
	 * @Column(length = 14) private String mobile2;
	 */
	
	@Column(length = 20)
	private String town;
	
	@Column(length = 20)
	private String taluk;
	
	@Column(length = 20)
	private String state;

	@Column(length = 20)
	private String country = "India";

	@Column(length = 6)
	private String zipCode;

}
