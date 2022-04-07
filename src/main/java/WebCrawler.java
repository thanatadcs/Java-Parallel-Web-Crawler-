import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class WebCrawler {

    ExecutorService executorService = Executors.newFixedThreadPool(32);

    public void crawlJavaDoc(String url, String filepath) {
        Bfs bfs = new Bfs(new HTMLParser(), executorService);
        Long startBfs = System.currentTimeMillis();
        Set<String> links = bfs.bfs(url); // Gather all links
        Long endBfs = System.currentTimeMillis();
        System.out.println("Done fetching all links, time: " + (endBfs - startBfs));
        System.out.println("links: " + links.size());

        Long startDl = System.currentTimeMillis();
        AtomicInteger finisher = new AtomicInteger(0);
        for (String link: links) {
            executorService.execute(() -> {
                URLDownloader.get(link, filepath);
                finisher.addAndGet(1);
                synchronized (finisher) {
                    finisher.notify();
                }
            });
        }

        while (finisher.get() < links.size()) {
            synchronized (finisher) {
                try {
                    finisher.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        executorService.shutdown();
        Long endDl = System.currentTimeMillis();
        System.out.println("Done download the files, time: " + (endDl - startDl));
    }
}
