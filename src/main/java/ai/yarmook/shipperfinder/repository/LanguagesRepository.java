package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.Languages;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Languages entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LanguagesRepository extends JpaRepository<Languages, Long> {}
