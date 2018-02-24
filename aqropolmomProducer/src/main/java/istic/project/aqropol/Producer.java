package istic.project.aqropol;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * Hello world!
 *
 */
public class Producer
{
    public static final String SENSOR_LOGS = "sensor_logs";

    public static void main( String[] args )
    {
        new Producer(Producer.SENSOR_LOGS);
    }

    public Producer(String exchange_name) {
        ConnectionFactory factory = new ConnectionFactory();

        try {
            factory.setUri("amqp://guest:guest@localhost:58883/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        //factory.setHost("localhost");
        Connection connection = null;


        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(exchange_name, BuiltinExchangeType.TOPIC);
            String queueName = channel.queueDeclare().getQueue();

            while(true) {
                channel.basicPublish(SENSOR_LOGS, "sensor.test", null, "a test".getBytes("UTF-8"));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

}
