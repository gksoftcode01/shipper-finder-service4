package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.TripItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class TripItemRepositoryWithBagRelationshipsImpl implements TripItemRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String TRIPITEMS_PARAMETER = "tripItems";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TripItem> fetchBagRelationships(Optional<TripItem> tripItem) {
        return tripItem.map(this::fetchTags);
    }

    @Override
    public Page<TripItem> fetchBagRelationships(Page<TripItem> tripItems) {
        return new PageImpl<>(fetchBagRelationships(tripItems.getContent()), tripItems.getPageable(), tripItems.getTotalElements());
    }

    @Override
    public List<TripItem> fetchBagRelationships(List<TripItem> tripItems) {
        return Optional.of(tripItems).map(this::fetchTags).orElse(Collections.emptyList());
    }

    TripItem fetchTags(TripItem result) {
        return entityManager
            .createQuery("select tripItem from TripItem tripItem left join fetch tripItem.tags where tripItem.id = :id", TripItem.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<TripItem> fetchTags(List<TripItem> tripItems) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, tripItems.size()).forEach(index -> order.put(tripItems.get(index).getId(), index));
        List<TripItem> result = entityManager
            .createQuery(
                "select tripItem from TripItem tripItem left join fetch tripItem.tags where tripItem in :tripItems",
                TripItem.class
            )
            .setParameter(TRIPITEMS_PARAMETER, tripItems)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
