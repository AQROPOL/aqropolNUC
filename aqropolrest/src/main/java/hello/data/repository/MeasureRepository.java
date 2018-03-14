package hello.data.repository;

import hello.data.Measure;
import hello.data.Nuc;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author VinYarD
 * created : 14/03/2018, 04:21
 */


public interface MeasureRepository extends PagingAndSortingRepository<Measure, Long> {

}
