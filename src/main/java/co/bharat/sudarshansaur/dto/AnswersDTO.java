package co.bharat.sudarshansaur.dto;

import co.bharat.sudarshansaur.entity.Questions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswersDTO {

	private Questions questions;
	
	private String answerText; 

}
