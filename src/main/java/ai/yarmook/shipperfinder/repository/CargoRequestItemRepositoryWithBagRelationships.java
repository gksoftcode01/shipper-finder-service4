package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.CargoRequestItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CargoRequestItemRepositoryWithBagRelationships {
    Optional<CargoRequestItem> fetchBagRelationships(Optional<CargoRequestItem> cargoRequestItem);

    List<CargoRequestItem> fetchBagRelationships(List<CargoRequestItem> cargoRequestItems);

    Page<CargoRequestItem> fetchBagRelationships(Page<CargoRequestItem> cargoRequestItems);
}
