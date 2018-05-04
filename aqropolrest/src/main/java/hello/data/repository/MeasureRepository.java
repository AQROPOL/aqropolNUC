package hello.data.repository;

import hello.data.repository.projection.InlineSensor;
import hello.data.Measure;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author VinYarD
 * created : 14/03/2018, 04:21
 */

@RepositoryRestResource(excerptProjection = InlineSensor.class)
public interface MeasureRepository extends PagingAndSortingRepository<Measure, Long> {

    Optional<Measure> findMeasureByHash(@Param("hash") byte[] hash);

    @Transactional
    void removeAllByIdBefore(Long id);
}
