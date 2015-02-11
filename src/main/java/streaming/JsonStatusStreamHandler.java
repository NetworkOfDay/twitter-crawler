package streaming;
import twitter4j.StatusListener;



interface JsonStatusStreamHandler extends StatusListener {
	public void onStatus(String json);
}
