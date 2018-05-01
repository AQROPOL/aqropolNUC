package hello.data;

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

    /**
     * @author VinYarD
     * created : 01/05/2018, 15:10
     */


    class IdMeasure {
    }
}
