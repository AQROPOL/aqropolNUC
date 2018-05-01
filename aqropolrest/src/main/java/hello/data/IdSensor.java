package hello.data;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "idSensor", types = { Sensor.class })
public interface IdSensor {
    long getId();
    String getName();
    String getType();
    String getUnity();
}
