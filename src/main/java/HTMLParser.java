import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class HTMLParser {

    public Set<String> getMoreValidURL(String urlSt) {
        Set<String> result = new HashSet<>();
        try {
            Document doc = Jsoup.connect(urlSt).get();
            URL url = new URL(urlSt);

            Set<String> results = new HashSet<>();
            addValidURLToList("a", "href", doc, url, result);
            addValidURLToList("img", "src", doc, url, result);
            addValidURLToList("link", "href", doc, url, result);
            addValidURLToList("script", "src", doc, url, result);
            addValidURLToList("iframe", "src", doc, url, result);
        } catch (MalformedURLException ex) {
            System.out.println("Is not html");
        } catch (IOException e) {

        }

        return result;
    }

    private void addValidURLToList(String tag, String attr, Document doc, URL baseUrl, Set<String> result) {
        Elements elements = doc.getElementsByTag(tag);
        for (Element e: elements) {
            String s = e.attr(attr);
            try {
                URL newUrl = new URL(baseUrl, s);
                if (newUrl.getHost().equals(baseUrl.getHost())) {
                    String newUrlSt = new URL(newUrl.getProtocol(), newUrl.getHost(), newUrl.getFile()).toExternalForm();
                    result.add(newUrlSt);
                }
            } catch (MalformedURLException ex) {
                // catch silently
            }
        }
    }
}
