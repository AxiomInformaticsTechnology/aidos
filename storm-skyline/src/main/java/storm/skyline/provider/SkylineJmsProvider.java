package storm.skyline.provider;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

import org.apache.storm.jms.JmsProvider;

import storm.skyline.messaging.SkylineBroker;

public class SkylineJmsProvider implements JmsProvider {

    private static final long serialVersionUID = 7111171996876882724L;

    private final Destination destination;

    public SkylineJmsProvider(String queue) {
        destination = (Destination) SkylineBroker.getContext().getBean(queue);
    }

    public ConnectionFactory connectionFactory() throws Exception {
        return SkylineBroker.getConnectionFactory();
    }

    public Destination destination() throws Exception {
        return destination;
    }

}