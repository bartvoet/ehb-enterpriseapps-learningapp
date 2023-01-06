package be.ehb.bvo.leanring.repo;

import be.ehb.bvo.leanring.model.ListQuestion;
import be.ehb.bvo.leanring.model.QuestionSeries;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface QuestionSeriesRepository extends CrudRepository<QuestionSeries, Integer> {

}