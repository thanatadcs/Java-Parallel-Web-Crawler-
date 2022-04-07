import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Bfs{

    HTMLParser parser;

    ExecutorService executorService;

    public Bfs(HTMLParser parser, ExecutorService executorService) {
        this.parser = parser;
        this.executorService = executorService;
    }

    private Set<String> expand(Set<String> frontier) {
        Set<String> newFrontier = new ConcurrentSkipListSet<>();
        AtomicInteger finisher = new AtomicInteger(0);

        for (String v: frontier) {
            executorService.execute(() -> {
                newFrontier.addAll(parser.getMoreValidURL(v));
                finisher.addAndGet(1);
                synchronized (finisher) {
                    finisher.notify();
                }
            });
        }

        while (finisher.get() < frontier.size()) {
            try {
                synchronized (finisher) {
                    finisher.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return newFrontier;
    }

    public Set<String> bfs(String src) {

        Set<String> frontier = new HashSet<>(List.of(src));
        Set<String> visited = new HashSet<>(List.of(src));

        while (!frontier.isEmpty()) {
            frontier = expand(frontier);
            frontier.removeAll(visited);
            visited.addAll(frontier);
        }

        return visited;
    }

}
