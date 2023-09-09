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
public class UserInfo {
	
	@Column(length = 40)
	private String name;

	@Column(length = 14)
	private String mobile;

	@Column(length = 30)
	private String place;

}
