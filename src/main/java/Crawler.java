import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import streaming.JsonTwitter4JClient;
import streaming.StreamListener;
import util.Utils;
import util.WDTDownloader;
import writer.JsonWriter;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import com.twitter.hbc.twitter4j.Twitter4jStatusClient;

public class Crawler {

    private Client client;
    private final Credential cred;
    private final int numProcessingThreads = 1;
    private final String filename;
    private BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);
    private StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();

    public Crawler(Credential cred, String filename) {
        this.cred = cred;
        this.filename = filename;
    }

    private void trackTerms(List<String> keywords) {
        endpoint.trackTerms(keywords);
    }

    private void filterLanguage(List<String> languages) {
        endpoint.languages(languages);
    }

    private Client oauth(String consumerKey, String consumerSecret,
                         String token, String secret) throws InterruptedException {

        Authentication auth = new OAuth1(consumerKey, consumerSecret, token,
                secret);

        return new ClientBuilder().hosts(Constants.STREAM_HOST)
                .endpoint(endpoint).authentication(auth)
                .processor(new StringDelimitedProcessor(queue)).build();

    }

    private Twitter4jStatusClient createClient() {
        ExecutorService executorService = Executors
                .newFixedThreadPool(numProcessingThreads);

        JsonTwitter4JClient t4jClient = new JsonTwitter4JClient(client,
                queue, Lists.newArrayList(new StreamListener(new JsonWriter(
                filename))), executorService);

        return t4jClient;
    }

    public void stream(List<String> keywords, List<String> languages) {
        try {
            client = oauth(cred.CONSUMER_KEY, cred.CONSUMER_SECRET, cred.TOKEN,
                    cred.SECRET);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        trackTerms(keywords);
        filterLanguage(languages);

        Twitter4jStatusClient t4jClient = createClient();

        t4jClient.connect();
        t4jClient.process();
    }

    public static void main(String[] args) {
        final Logger log = Logger.getLogger("InfoLogging");

        String path = "";
        if (args.length == 0) {
            log.info("Usage: java -jar twitter-crawler.jar pathToSaveDir \n"
                    + "Note: The pathToSaveDir needs a trailing forward slash e.g /home/toa/Desktop/ \n"
                    + "Using default directory to store tweets");
        } else {
            path = args[0];
        }

        List<String> keywords = new WDTDownloader().getKeywordsForToday();
        if (keywords.isEmpty()) {
            log.info("Page \"Wort des Tages\" is down...");
            return;
        }

        String timestamp = Utils.getTimestamp(Utils.today());
        log.info("Tracking " + keywords.size() + " keywords for " + timestamp);
        System.out.println(path + timestamp);
		//Your own credentials   
		Credential cred = new Credential("?????", "????", "???");

        Crawler crawler = new Crawler(cred, path + timestamp);
        crawler.stream(keywords, Lists.newArrayList("de"));
    }
}
