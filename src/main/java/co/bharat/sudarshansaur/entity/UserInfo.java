package co.bharat.sudarshansaur.entity;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
	
	private String name;

	private String mobile;

	private String place;

}
