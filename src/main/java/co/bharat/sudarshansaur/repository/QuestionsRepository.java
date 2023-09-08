package co.bharat.sudarshansaur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import co.bharat.sudarshansaur.entity.Questions;

@RepositoryRestResource(collectionResourceRel = "questions", path = "questions")
public interface QuestionsRepository extends JpaRepository<Questions,Long>{
}
