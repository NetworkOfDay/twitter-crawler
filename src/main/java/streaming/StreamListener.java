package streaming;

import java.util.Arrays;
import java.util.logging.Logger;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import writer.JsonWriter;

import com.twitter.hbc.twitter4j.message.DisconnectMessage;
import com.twitter.hbc.twitter4j.message.StallWarningMessage;

public class StreamListener implements JsonStatusStreamHandler {

    private final JsonWriter writer;
    private final Logger log = Logger.getLogger("InfoLogging");

    public StreamListener(JsonWriter jsonWriter) {
        this.writer = jsonWriter;
    }

    public void onDeletionNotice(StatusDeletionNotice arg0) {
        //Nothing here
    }

    public void onScrubGeo(long arg0, long arg1) {
        //Nothing here
    }

    public void onStallWarning(StallWarning message) {
        log.info(message.getMessage());
    }

    public void onStatus(Status tweet) {
        //We use the raw json format instead of the parsed status
    }

    public void onStatus(String json) {
        writer.writeTweet(json);
    }

    public void onTrackLimitationNotice(int message) {
        log.info("Streaming Client RateLimit " + message);
    }

    public void onException(Exception message) {
        log.info(Arrays.toString(message.getStackTrace()));
        log.info(message.getMessage());
    }

    public void onDisconnectMessage(DisconnectMessage message) {
        log.info(message.getDisconnectReason());
    }

    public void onStallWarningMessage(StallWarningMessage message) {
        log.info(message.getMessage());

    }

    public void onUnknownMessageType(String message) {
        log.info(message);
    }
}
