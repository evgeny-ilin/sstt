package club.beingsoft.sstt.model;

import club.beingsoft.sstt.repository.WordJpaRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@Component
public class WordsModel {
    private static final Logger log = LoggerFactory.getLogger(WordsModel.class);
    private URL url;
    private int chunk;

    @Autowired
    private WordJpaRepository repository;

    public void prepare(URL url) {
        this.url = url;
        calcChunk();
    }

    public Map<String, Integer> countWords() {
        LoadData();
        log.info("Loading words from database");
        Map<String, Integer> wordsMap = new TreeMap<>();
        repository.findAll().forEach(word -> wordsMap.put(word.getWord_key(), word.getCount()));
        return wordsMap;
    }

    private void LoadData() {
        log.info("Starting loading page {}", url);
        try (BufferedInputStream in = new BufferedInputStream(url.openStream())) {
            byte dataBuffer[] = new byte[chunk];
            int bytesRead;
            int totalBytesLoad = 0;
            // Read bytes by chunk
            while ((bytesRead = in.read(dataBuffer, 0, chunk)) != -1) {
                // Read last bytes to ">" symbol for closing tag or to the end of file
                totalBytesLoad += bytesRead;
                log.info("{} bytes loaded", totalBytesLoad);
                List<Byte> byteList = new ArrayList<>();
                if (bytesRead != -1) {
                    int data;
                    while (true) {
                        data = in.read();
                        if (data == -1) break;
                        byteList.add((byte) data);
                        if (data == 62) break;
                    }
                }
                SaveWords(dataBuffer, byteList);
            }
        } catch (IOException e) {
            log.error("Can't load data", e);
            System.exit(-1);
        }
    }

    private void SaveWords(byte[] dataBuffer, List<Byte> bytesList) {
        // Make bytes array without null
        List<Byte> data = new ArrayList<>();
        for (int i = 0; i < dataBuffer.length; i++) {
            if (dataBuffer[i] == 0) break;
            data.add(dataBuffer[i]);
        }
        data.addAll(bytesList);
        byte[] bytesArray = new byte[data.size()];

        for (int i = 0; i < data.size(); i++) {
            bytesArray[i] = data.get(i);
        }

        String html = new String(bytesArray);
        Document document = Jsoup.parse(html);

        List<String> words = Arrays.asList(document.body()
                .text()
                .split("[[ ]*|[,]*|[\\.]*|[!]*|[?]*|[\"]*|[;]*|[:]*|[\\]]*|[\\]]*|[\\(]*|[\\)]*|[\n]*|[\r]*|[\t]]+")
        );

        Map<String, Integer> countedWords = new TreeMap<>();
        words.forEach(word -> countedWords.merge(word.toUpperCase(), 1, Integer::sum));

        log.info("{} words parsed", countedWords.size());

        // If we have word in database then getting it, calc new count and save
        countedWords.forEach((key, value) -> {
            Optional<Word> wordDb = repository.findById(key);
            Word word;
            if (wordDb.isPresent()) {
                word = wordDb.get();
                word.setCount(word.getCount() + value);
            } else word = new Word(key, value);
            repository.save(word);
        });

        log.info("{} words saved to database", countedWords.size());
    }

    private void calcChunk() {
        URLConnection conn = null;
        int docSize;
        try {
            conn = url.openConnection();
            docSize = conn.getContentLength();
        } catch (IOException e) {
            log.error("Can't get size of document", e);
            throw new RuntimeException(e);
        } finally {
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).disconnect();
            }
        }

        long freeMem = Runtime.getRuntime().freeMemory();
        if (freeMem > docSize || docSize > 0) chunk = docSize;
        else chunk = (int) freeMem / 2;
    }
}
