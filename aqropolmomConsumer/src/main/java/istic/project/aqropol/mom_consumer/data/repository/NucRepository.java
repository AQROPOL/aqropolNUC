package istic.project.aqropol.mom_consumer.data.repository;

import istic.project.aqropol.mom_consumer.data.Nuc;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author VinYarD
 * created : 14/03/2018, 04:20
 */


public interface NucRepository extends CrudRepository<Nuc, Long> {

    Optional<Nuc> findByToken(@Param("token") String token);
}
