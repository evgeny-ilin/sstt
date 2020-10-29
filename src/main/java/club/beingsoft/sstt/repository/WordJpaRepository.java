package club.beingsoft.sstt.repository;

import club.beingsoft.sstt.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordJpaRepository extends JpaRepository<Word, String> {
}
