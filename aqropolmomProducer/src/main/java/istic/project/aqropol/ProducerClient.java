package istic.project.aqropol;

import com.rabbitmq.client.BlockedListener;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

class ProducerClient {

    private static final Logger logger = Logger.getLogger(ProducerClient.class);

    private static ProducerClient producerClient;

    private Connection connection;

    private ProducerClient() throws IOException, TimeoutException {
        InputStream isConfig = TopicProducer.class.getResourceAsStream("/config.properties");
        Properties config = new Properties();

        try {
            config.load(isConfig);
            isConfig.close();
        } catch (IOException e) {
            logger.error("Can't load config : " + e.getMessage(), e);
        }

        ConnectionFactory factory = new ConnectionFactory();

        config.computeIfPresent("aqropol.amqp.vhost", (k, v) -> { factory.setVirtualHost((String) v); return v;});
        config.computeIfPresent("aqropol.amqp.user", (k, v) -> { factory.setUsername((String) v); return v; });
        config.computeIfPresent("aqropol.amqp.pass", (k, v) -> { factory.setPassword((String) v); return v;});
        config.computeIfPresent("aqropol.amqp.host", (k, v) -> { factory.setHost((String) v); return v;});
        config.computeIfPresent("aqropol.amqp.port", (k, v) -> { factory.setPort(Integer.valueOf((String) v)); return v;});


        factory.setAutomaticRecoveryEnabled(true);
        factory.setRequestedHeartbeat(60);

        this.connection = factory.newConnection();

        if(logger.isInfoEnabled()) {
            logger.info("RabbitMQ client connected");
            logger.info("[RabbitMQ Server] " + this.connection.getServerProperties());
            logger.info("[RabbitMQ Client] " + this.connection.getClientProperties());
        }

        this.connection.addBlockedListener(new BlockedListener() {
            @Override
            public void handleBlocked(String reason) throws IOException {
                logger.warn("RabbitMQ connection blocked : " + reason);
            }

            @Override
            public void handleUnblocked() throws IOException {
                logger.info("RabbitMQ connection unblocked");
            }
        });
    }

    public static ProducerClient getInstance() throws IOException, TimeoutException {

        if(ProducerClient.producerClient == null) {
            ProducerClient.producerClient = new ProducerClient();
        }

        return ProducerClient.producerClient;
    }

    public Channel newChannel() throws IOException {
        return this.connection.createChannel();
    }
}
