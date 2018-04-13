package istic.project.aqropol.mom_consumer;

import istic.project.aqropol.mom_consumer.data.repository.MeasureRepository;
import istic.project.aqropol.mom_consumer.data.repository.NucRepository;
import istic.project.aqropol.mom_consumer.data.repository.SensorRepository;
import org.springframework.amqp.core.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MomConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MomConsumerApplication.class, args);
    }

    @Bean
    public DirectExchange direct() {
        return new DirectExchange("aqropol_sensors");
    }

    @Bean
    public Queue autoDeleteQueue() {
        return new Queue("sensors");
    }

    @Bean
    public Binding binding(DirectExchange direct,
                             Queue autoDeleteQueue) {
        return BindingBuilder.bind(autoDeleteQueue)
                .to(direct)
                .with("sensors");
    }

    @Bean
    public Receiver receiver(MeasureRepository measureRepository, SensorRepository sensorRepository, NucRepository nucRepository) {
        return new Receiver(measureRepository, sensorRepository, nucRepository);
    }
}
