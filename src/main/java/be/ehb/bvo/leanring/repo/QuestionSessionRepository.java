package be.ehb.bvo.leanring.repo;

import be.ehb.bvo.leanring.model.QuestionSeries;
import be.ehb.bvo.leanring.model.QuestionSession;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface QuestionSessionRepository extends CrudRepository<QuestionSession, Integer> {

}