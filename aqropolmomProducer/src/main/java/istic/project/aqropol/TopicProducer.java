package istic.project.aqropol;

import com.rabbitmq.client.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

@Deprecated
public class TopicProducer {

    private final static Logger logger = Logger.getLogger(TopicProducer.class);

    private final String exchanger, root, encoding;
    private final Channel channel;

    private TopicProducer(Channel channel, String exchanger, String root, String encoding) {
        this.channel = channel;
        this.exchanger = exchanger;
        this.root = root;
        this.encoding = encoding;

        try {
            this.channel.exchangeDeclare(this.exchanger, BuiltinExchangeType.TOPIC, true);
        } catch (IOException e) {
            logger.error("Can't declare exchange : " + e.getMessage(), e);
        }

        if(logger.isDebugEnabled()) {
            this.channel.addReturnListener((replyCode, replyText, exchange, routingKey, properties, body) -> logger.debug("handleReturn(int replyCode = " + replyCode + ", String replyText = " + replyText + ", String exchange = " + exchange + ", String routingKey = " + routingKey + ", AMQP.BasicProperties properties = " + properties.toString() + ", byte[] body = " + new String(body) +";"));
        }

        this.channel.addShutdownListener(cause -> logger.warn("RabbitMQ Server has shutdown : " + cause));
    }

    public void addReturnListener(ReturnListener listener) {
        this.channel.addReturnListener(listener);
    }

    public void addConfirmListener(ConfirmListener listener) {
        this.channel.addConfirmListener(listener);
    }

    public void addShutdownListener(ShutdownListener listener) {
        this.channel.addShutdownListener(listener);
    }

    public Channel getChannel() {
        return this.channel;
    }

    public void send(String subTopic, String message) {
        try {
            if (this.channel.isOpen()) {
                this.channel.basicPublish(this.exchanger, this.root + "." + subTopic, false, null, message.getBytes(encoding));
            }
        } catch (IOException e) {
            logger.error("Can't publish to broker : " + e.getMessage(), e);
        }
    }

    public String getRootTopic() {
        return this.root;
    }

    public String getExchangeName() {
        return this.exchanger;
    }

    public static class TopicProducerFactory {

        private Properties config = new Properties();
        private String exchanger = "";
        private String root = "";
        private String encoding = "";

        public TopicProducerFactory() {
            InputStream isConfig = TopicProducer.class.getResourceAsStream("/config.properties");
            this.config = new Properties();

            try {
                config.load(isConfig);

                this.exchanger = config.getProperty("aqropol.amqp.exchange", "");
                this.root = config.getProperty("aqropol.amqp.topic.root", "");
                this.encoding = config.getProperty("project.build.sourceEncoding", "UTF-8");

                isConfig.close();
            } catch (IOException e) {
                logger.error("Can't load config : " + e.getMessage(), e);
            }

        }

        public TopicProducerFactory setExchanger(String name) {
            this.exchanger = name;
            return this;
        }

        public TopicProducerFactory setRoot(String topic) {
            this.root = topic;
            return this;
        }

        public TopicProducer build() throws IOException, TimeoutException {
            return new TopicProducer(ProducerClient.getInstance().newChannel(), exchanger, root, encoding);
        }
    }
}