import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 31.03.2017.
 */
public class web {
    private static void download(URI link, HashSet<URI> visited) throws IOException {
        if (visited.contains(link)||visited.size()>=100)
            return;
        visited.add(link);
        System.out.println(link);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(link);
        try (CloseableHttpResponse resp = client.execute(get)) {
            String html = EntityUtils.toString(resp.getEntity());
            Pattern p = Pattern.compile("href\\s*=\\s*([^ >]+|\"[^\"]*\"|'[^']*')");
            Matcher m = p.matcher(html);
            while(m.find()){
                String href = m.group(1);

                if (href.startsWith("\"")) {
                    href = href.substring(1,href.length()-1);
                }

                URI child=link.resolve(href.trim());
                System.out.println(child);
                download(child,visited);
            }
        }
        client.close();



//        URL url = link.toURL();
//        String html="";
//        String href="";
//        URI child=link.resolve(href);
//        System.out.println(child);
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        HashSet<URI> visited = new HashSet<URI>();
        web.download(new URI ("https://cyber.mirea.ru/"),visited);
    }
}
