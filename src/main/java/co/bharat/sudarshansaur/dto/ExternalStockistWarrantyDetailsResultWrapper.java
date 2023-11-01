package co.bharat.sudarshansaur.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalStockistWarrantyDetailsResultWrapper {

	@JsonProperty("Serial")
	private List<ExternalWarrantyDetailsDTO> serial;

}
