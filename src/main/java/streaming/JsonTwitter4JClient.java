package streaming;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import twitter4j.JSONException;
import twitter4j.Status;
import twitter4j.TwitterException;

import com.google.common.collect.ImmutableList;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.twitter4j.Twitter4jStatusClient;


public class JsonTwitter4JClient extends Twitter4jStatusClient {

    private final List<? extends JsonStatusStreamHandler> statusListeners;
    private String currentJsonTweet = "";

    public JsonTwitter4JClient(Client client,
                               BlockingQueue<String> blockingQueue,
                               List<? extends JsonStatusStreamHandler> listeners,
                               ExecutorService executorService) {

        super(client, blockingQueue, listeners, executorService);
        this.statusListeners = ImmutableList.copyOf(listeners);
    }

    @Override
    protected void parseMessage(String msg) throws JSONException,
            TwitterException, IOException {

        currentJsonTweet = msg;
        super.parseMessage(msg);
    }

    @Override
    protected void onStatus(long siteStreamUser, final Status status) {
        for (JsonStatusStreamHandler listener : statusListeners) {
            listener.onStatus(currentJsonTweet);
        }
    }
}
