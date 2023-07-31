package co.bharat.sudarshansaur.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusCountDTO {

    private String entity;
    private String status;
    private Long count;

}
