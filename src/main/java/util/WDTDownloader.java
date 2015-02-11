package util;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.common.collect.Lists;


public class WDTDownloader {
	
	private final String url = "http://wortschatz.uni-leipzig.de/wort-des-tages/";

	private List<String> splitKeywords(List<String> list) {
		
		List<String> keywords = Lists.newArrayList();
		//Split names to track both parts e.g  "Manuel Neuer" -> "Manuel" and "Neuer"
		for (String k : list) {
			keywords.addAll(Arrays.asList(k.split(" ")));
		}
		return keywords;
	}
	
	public List<String> getKeywordsForToday() {
		
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			//"Wort des Tages" Page is not reachable
			return Lists.newArrayList();
		}
		
		List<String> keywords = Lists.newArrayList();
		
		Element table = doc.select("table").first();
		for(Element links : table.select("a:not(target)")) {
			keywords.add(links.text());
		}
        
		//Remove static non keywords from list e.g "Wortschatz" and "Wörter des Tages"
		keywords = keywords.subList(2	, keywords.size() - 2);
		return splitKeywords(keywords);	
	}
}
