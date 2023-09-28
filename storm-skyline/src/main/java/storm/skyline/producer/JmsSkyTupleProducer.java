package storm.skyline.producer;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import com.skyline.model.BasicSkyTuple;

public interface JmsSkyTupleProducer extends Serializable {

    public Message toMessage(Session session, BasicSkyTuple input) throws JMSException;

}