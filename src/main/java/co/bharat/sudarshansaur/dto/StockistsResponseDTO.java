package co.bharat.sudarshansaur.dto;

import java.util.Date;
import java.util.List;

import co.bharat.sudarshansaur.entity.Address;
import co.bharat.sudarshansaur.entity.Dealers;
import co.bharat.sudarshansaur.enums.UserStatus;
import co.bharat.sudarshansaur.interfaces.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockistsResponseDTO implements Users{

	private long stockistId;

	private String stockistName;

	private String mobileNo;

	private UserStatus status;

	private String email;

	private Address address;

	private Date createdOn;

	private Date updatedOn;

	private Date lastLogin;

	private String image;

	private String businessAddress;

	private String businessName;

	private String gstNumber;

	private List<Dealers> dealers;
	
	private String stockistCode;

}
