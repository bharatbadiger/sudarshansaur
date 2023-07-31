package co.bharat.sudarshansaur.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import co.bharat.sudarshansaur.interfaces.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin implements Users{

	@Id
	private String mobileNo;
}
