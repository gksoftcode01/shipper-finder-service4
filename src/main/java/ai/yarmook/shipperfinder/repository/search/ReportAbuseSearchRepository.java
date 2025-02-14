package ai.yarmook.shipperfinder.repository.search;

import ai.yarmook.shipperfinder.domain.ReportAbuse;
import ai.yarmook.shipperfinder.repository.ReportAbuseRepository;
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
 * Spring Data Elasticsearch repository for the {@link ReportAbuse} entity.
 */
public interface ReportAbuseSearchRepository extends ElasticsearchRepository<ReportAbuse, Long>, ReportAbuseSearchRepositoryInternal {}

interface ReportAbuseSearchRepositoryInternal {
    Page<ReportAbuse> search(String query, Pageable pageable);

    Page<ReportAbuse> search(Query query);

    @Async
    void index(ReportAbuse entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ReportAbuseSearchRepositoryInternalImpl implements ReportAbuseSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ReportAbuseRepository repository;

    ReportAbuseSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ReportAbuseRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<ReportAbuse> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<ReportAbuse> search(Query query) {
        SearchHits<ReportAbuse> searchHits = elasticsearchTemplate.search(query, ReportAbuse.class);
        List<ReportAbuse> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(ReportAbuse entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ReportAbuse.class);
    }
}
