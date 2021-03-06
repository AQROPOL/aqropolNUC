package istic.project.aqropol.mom_consumer;

import istic.project.aqropol.mom_consumer.data.repository.MeasureRepository;
import istic.project.aqropol.mom_consumer.data.repository.NucRepository;
import istic.project.aqropol.mom_consumer.data.repository.SensorRepository;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MomConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MomConsumerApplication.class, args);
    }

    /*
        Les variables @Value sont initalise grace aux ressources presentent dans le fichier application.properties
     */
    @Value("${aqropol.amqp.channel.exchanger}")
    private String exchanger;

    @Value("${aqropol.amqp.channel.route}")
    private String route;

    @Value("${aqropol.amqp.queue}")
    private String queue;

    /*
        Voir documentation : https://www.rabbitmq.com/tutorials/tutorial-four-spring-amqp.html
     */

    @Bean
    public DirectExchange direct() {
        return new DirectExchange(exchanger);
    }

    @Bean
    public Queue autoDeleteQueue() {
        return new Queue(queue);
    }

    @Bean
    public Binding binding(DirectExchange direct,
                             Queue autoDeleteQueue) {
        return BindingBuilder.bind(autoDeleteQueue)
                .to(direct)
                .with(route);
    }

    @Bean
    public Receiver receiver(MeasureRepository measureRepository, SensorRepository sensorRepository, NucRepository nucRepository) {
        return new Receiver(measureRepository, sensorRepository, nucRepository);
    }
}
