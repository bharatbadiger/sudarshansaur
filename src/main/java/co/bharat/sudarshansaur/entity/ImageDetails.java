package co.bharat.sudarshansaur.entity;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDetails {
	
	private String imgLiveSystem;

	private String imgSystemSerialNo;

	private String imgAadhar;

}
