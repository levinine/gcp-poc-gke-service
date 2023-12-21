package gcp.poc.weatherdata.pubsub;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.pubsub.v1.PubsubMessage;
import gcp.poc.weatherdata.service.PubSubMessageResolverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Component
public class MessageFetcher {

    @Autowired
    Logger logger;

    @Value("${spring.cloud.gcp.pubsub.subscriber.fully-qualified-name}")
    private String subscriptionName;

    @Autowired
    private PubSubTemplate pubSubTemplate;

    @Autowired
    private PubSubMessageResolverService pubSubMessageResolverService;

    @Scheduled(fixedRate = 600000)
    public void fetchMessage() throws IOException {
        logger.info(String.format("Trying to fetch message at MessageFetcher.fetchMessage() from %s", subscriptionName.split("/")[3]));
        List<PubsubMessage> messageList = pubSubTemplate.pullAndAck(
                subscriptionName.split("/")[3],
                2,
                false);
        for(PubsubMessage psm : messageList) {
            pubSubMessageResolverService.resolveMessageAndSave(psm.getData().toStringUtf8());
        }
    }
}