package be.ehb.bvo.leanring.repo;

import be.ehb.bvo.leanring.model.ListQuestion;
import be.ehb.bvo.leanring.model.User;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface QuestionRepository extends CrudRepository<ListQuestion, Integer> {

}