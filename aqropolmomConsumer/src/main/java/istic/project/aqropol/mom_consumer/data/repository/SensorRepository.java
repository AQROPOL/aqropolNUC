package hello.data.repository;

import hello.data.Sensor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author VinYarD
 * created : 14/03/2018, 01:56
 */


public interface SensorRepository extends PagingAndSortingRepository<Sensor, Long> {

    List<Sensor> findAllByType(@Param("type") String type);

}
