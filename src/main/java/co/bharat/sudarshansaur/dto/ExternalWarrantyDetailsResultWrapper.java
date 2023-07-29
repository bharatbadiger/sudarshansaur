package co.bharat.sudarshansaur.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalWarrantyDetailsResultWrapper {

	private List<ExternalWarrantyDetailsDTO> results;

}
