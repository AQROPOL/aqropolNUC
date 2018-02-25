package istic.project.aqropol;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

/**
 * Hello world!
 *
 */
public class Producer
{
    private Properties config = new Properties();
    private ConnectionFactory factory;

    private String exchange_name = "";
    private String root_topic = "";

    public static void main( String[] args )
    {
        new Producer();
    }

    @Override
    public String toString() {
        return "istic.project.aqropol.Producer{" +
                "exchange_name='" + exchange_name + '\'' +
                ", root_topic='" + root_topic + '\'' +
                ", virtualHost='" + getVirtualHost() + '\'' +
                ", host='" + getHost() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", port=" + getPort() +
                ", exchange='" + getExchange() + '\'' +
                '}';
    }

    public Producer() {
        InputStream isConfig = Producer.class.getResourceAsStream("/config.properties");

        try {
            config.load(isConfig);

            isConfig.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.factory = new ConnectionFactory();

        config.computeIfPresent("aqropol.amqp.exchange", (k, v) -> setExchange((String) v));
        config.computeIfPresent("aqropol.amqp.topic.root", (k, v) -> setRoot((String) v));
        config.computeIfPresent("aqropol.amqp.vhost", (k, v) -> setVirtualHost((String) v));
        config.computeIfPresent("aqropol.amqp.user", (k, v) -> setUsername((String) v));
        config.computeIfPresent("aqropol.amqp.pass", (k, v) -> setPassword((String) v));
        config.computeIfPresent("aqropol.amqp.host", (k, v) -> setHost((String) v));
        config.computeIfPresent("aqropol.amqp.port", (k, v) -> setPort(Integer.valueOf((String) v)));

        Connection connection = null;

        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(exchange_name, BuiltinExchangeType.TOPIC);
            String queueName = channel.queueDeclare().getQueue();

            while(true) {
                channel.basicPublish(this.exchange_name, "sensor.test", null, "a test".getBytes("UTF-8"));
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

    public Producer setRoot(String root_topic) {
        this.root_topic = root_topic;
        return this;
    }

    public Producer setVirtualHost(String vhost) {
        this.factory.setVirtualHost(vhost);
        return this;
    }

    public String getVirtualHost() {
        return this.factory.getVirtualHost();
    }

    public Producer setHost(String host) {
        this.factory.setHost(host);
        return this;
    }

    public String getHost() {
        return this.factory.getHost();
    }

    public Producer setUsername(String user) {
        this.factory.setUsername(user);
        return this;
    }

    public String getUsername() {
        return this.factory.getUsername();
    }

    public Producer setPassword(String pass) {
        this.factory.setPassword(pass);
        return this;
    }

    public String getPassword() {
        return this.factory.getPassword();
    }

    public Producer setPort(int port) {
        this.factory.setPort(port);
        return this;
    }

    public int getPort() {
        return this.factory.getPort();
    }

    public Producer setExchange(String exchangeName) {
        this.exchange_name = exchangeName;
        return this;
    }

    public String getExchange() {
        return this.exchange_name;
    }
}