package hello.data.repository;

import hello.data.Nuc;
import hello.data.Sensor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author VinYarD
 * created : 14/03/2018, 04:20
 */


public interface NucRepository extends PagingAndSortingRepository<Nuc, Long> {

}
