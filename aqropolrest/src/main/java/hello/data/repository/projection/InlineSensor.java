package hello.data.repository.projection;

import hello.data.Measure;
import hello.data.Nuc;
import hello.data.Sensor;
import org.springframework.data.rest.core.config.Projection;

import java.sql.Timestamp;

@Projection(name = "inlineSensor", types = { Measure.class })
public interface InlineSensor {

    Sensor getSensor();
    long getId();
    byte[] getHash();
    byte[] getValue();
    Timestamp getTimestamp();
    Nuc getNuc();
}
