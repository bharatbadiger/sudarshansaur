package co.bharat.sudarshansaur.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusCountDTO {

	private Map<String, MergedStatusForEntity> data;

}
