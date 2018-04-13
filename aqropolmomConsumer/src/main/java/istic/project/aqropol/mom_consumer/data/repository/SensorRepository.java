package istic.project.aqropol.mom_consumer.data.repository;

import istic.project.aqropol.mom_consumer.data.Sensor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author VinYarD
 * created : 14/03/2018, 01:56
 */


public interface SensorRepository extends CrudRepository<Sensor, Long> {

    List<Sensor> findAllByType(@Param("type") String type);

    Optional<Sensor> findByNameAndTypeAndUnity(@Param("name") String name, @Param("type") String type, @Param("unity") String unity);

}
