package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.TripItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TripItemRepositoryWithBagRelationships {
    Optional<TripItem> fetchBagRelationships(Optional<TripItem> tripItem);

    List<TripItem> fetchBagRelationships(List<TripItem> tripItems);

    Page<TripItem> fetchBagRelationships(Page<TripItem> tripItems);
}
