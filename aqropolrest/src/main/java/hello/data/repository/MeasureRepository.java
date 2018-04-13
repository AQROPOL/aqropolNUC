package hello.data.repository;

import hello.data.Measure;
import hello.data.Nuc;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author VinYarD
 * created : 14/03/2018, 04:21
 */


public interface MeasureRepository extends PagingAndSortingRepository<Measure, Long> {

    Optional<Measure> findMeasureByHash(@Param("hash") byte[] hash);

    @Transactional
    void removeAllByIdBefore(Long id);
}
