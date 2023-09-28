package storm.skyline.messaging;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyline.model.BasicSkyTuple;

import storm.skyline.producer.JmsSkyTupleProducer;
import storm.skyline.provider.SkylineJmsProvider;

public class MessengerFactory {

    private final static boolean AUTO_ACK = true;

    private final static boolean JMS_TRANSACTIONAL = false;

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static Messenger configure(Messenger messenger) {

        messenger.setAutoAck(AUTO_ACK);
        messenger.setJmsTransactional(JMS_TRANSACTIONAL);
        messenger.setJmsAcknowledgeMode(AUTO_ACKNOWLEDGE);

        messenger.setJmsProvider(createSkylineJmsProvider(messenger.getQueueConfig().getName()));

        messenger.setJmsSkyTupleProducer(new JmsSkyTupleProducer() {
            private static final long serialVersionUID = 6877437439909118806L;

            @Override
            public Message toMessage(Session session, BasicSkyTuple skyTuple) throws JMSException {
                return session.createTextMessage(objectMapper.valueToTree(skyTuple).toString());
            }
        });

        return messenger;
    }

    private static SkylineJmsProvider createSkylineJmsProvider(String queue) {
        return new SkylineJmsProvider(queue);
    }

}