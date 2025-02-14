package ai.yarmook.shipperfinder.repository.search;

import ai.yarmook.shipperfinder.domain.Languages;
import ai.yarmook.shipperfinder.repository.LanguagesRepository;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Languages} entity.
 */
public interface LanguagesSearchRepository extends ElasticsearchRepository<Languages, Long>, LanguagesSearchRepositoryInternal {}

interface LanguagesSearchRepositoryInternal {
    Page<Languages> search(String query, Pageable pageable);

    Page<Languages> search(Query query);

    @Async
    void index(Languages entity);

    @Async
    void deleteFromIndexById(Long id);
}

class LanguagesSearchRepositoryInternalImpl implements LanguagesSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final LanguagesRepository repository;

    LanguagesSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, LanguagesRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Languages> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Languages> search(Query query) {
        SearchHits<Languages> searchHits = elasticsearchTemplate.search(query, Languages.class);
        List<Languages> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Languages entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Languages.class);
    }
}
