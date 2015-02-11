
public class Credential {
    public final String CONSUMER_KEY;
    public final String CONSUMER_SECRET;
    public final String TOKEN;
    public final String SECRET;

    public Credential(String consumerKey, String consumerSecret, String token, String secret) {
        this.CONSUMER_KEY = consumerKey;
        this.CONSUMER_SECRET = consumerSecret;
        this.TOKEN = token;
        this.SECRET = secret;
    }
}
