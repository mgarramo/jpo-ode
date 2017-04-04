package us.dot.its.jpo.ode.coder;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import us.dot.its.jpo.ode.OdeProperties;
import us.dot.its.jpo.ode.SerializableMessageProducerPool;
import us.dot.its.jpo.ode.plugin.PluginFactory;
import us.dot.its.jpo.ode.plugin.asn1.Asn1Plugin;
import us.dot.its.jpo.ode.wrapper.MessageProducer;

@RunWith(JMockit.class)
public class AbstractCoderTest2 {

    @Tested
    BsmCoder testBsmCoder;
    @Injectable
    OdeProperties mockOdeProperties;

    @Mocked
    Asn1Plugin mockAsn1Plugin;

    //@SuppressWarnings("rawtypes")
    @Test
    public void testPublish(@Mocked final PluginFactory mockPluginFactory,
            @Mocked final MessageProducer mockMessageProducer,
            @Mocked final SerializableMessageProducerPool mockSerializableMessageProducerPool) {

        testBsmCoder.publish("testTopic", "testMessage");
    }

}