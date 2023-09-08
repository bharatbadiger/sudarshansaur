package co.bharat.sudarshansaur.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Questions {

	@Id
	@GeneratedValue
	private long questionId;

	private String questionText;
	
	private String options;

}
