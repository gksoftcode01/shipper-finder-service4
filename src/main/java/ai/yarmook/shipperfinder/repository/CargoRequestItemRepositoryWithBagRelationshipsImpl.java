package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.CargoRequestItem;
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
public class CargoRequestItemRepositoryWithBagRelationshipsImpl implements CargoRequestItemRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String CARGOREQUESTITEMS_PARAMETER = "cargoRequestItems";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CargoRequestItem> fetchBagRelationships(Optional<CargoRequestItem> cargoRequestItem) {
        return cargoRequestItem.map(this::fetchTags);
    }

    @Override
    public Page<CargoRequestItem> fetchBagRelationships(Page<CargoRequestItem> cargoRequestItems) {
        return new PageImpl<>(
            fetchBagRelationships(cargoRequestItems.getContent()),
            cargoRequestItems.getPageable(),
            cargoRequestItems.getTotalElements()
        );
    }

    @Override
    public List<CargoRequestItem> fetchBagRelationships(List<CargoRequestItem> cargoRequestItems) {
        return Optional.of(cargoRequestItems).map(this::fetchTags).orElse(Collections.emptyList());
    }

    CargoRequestItem fetchTags(CargoRequestItem result) {
        return entityManager
            .createQuery(
                "select cargoRequestItem from CargoRequestItem cargoRequestItem left join fetch cargoRequestItem.tags where cargoRequestItem.id = :id",
                CargoRequestItem.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<CargoRequestItem> fetchTags(List<CargoRequestItem> cargoRequestItems) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, cargoRequestItems.size()).forEach(index -> order.put(cargoRequestItems.get(index).getId(), index));
        List<CargoRequestItem> result = entityManager
            .createQuery(
                "select cargoRequestItem from CargoRequestItem cargoRequestItem left join fetch cargoRequestItem.tags where cargoRequestItem in :cargoRequestItems",
                CargoRequestItem.class
            )
            .setParameter(CARGOREQUESTITEMS_PARAMETER, cargoRequestItems)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
