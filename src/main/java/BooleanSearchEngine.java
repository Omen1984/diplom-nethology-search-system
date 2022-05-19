import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BooleanSearchEngine implements SearchEngine {
    private Map<String, List<PageEntry>> wordMap;

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        wordMap = new TreeMap<>();
        Map<String, PdfDocument> pdfDocumentMap = returnMapPdfFromDir(pdfsDir);
        fillingWordMap(pdfDocumentMap);
    }

    private void fillingWordMap(Map<String, PdfDocument> pdfDocumentMap) {
        for (Map.Entry<String, PdfDocument> doc : pdfDocumentMap.entrySet()) {
            String pageEntryName = doc.getKey();
            int page = doc.getValue().getNumberOfPages();

            for (int i = 1; i <= page; i++) {
                Map<String, Integer> freqs = new HashMap<>();
                PdfPage pdfPage = doc.getValue().getPage(i);

                var text = PdfTextExtractor.getTextFromPage(pdfPage);
                var words = text.split("\\P{IsAlphabetic}+");

                for (String word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    freqs.put(word.toLowerCase(), freqs.getOrDefault(word, 0) + 1);
                }

                for (Map.Entry<String, Integer> entry : freqs.entrySet()) {
                    int openPage = i;
                    int count = entry.getValue();
                    String word = entry.getKey();
                    PageEntry pageEntry = new PageEntry(pageEntryName, openPage, count);

                    if (!wordMap.containsKey(word)) {
                        wordMap.put(word, new ArrayList<>());
                    }
                    wordMap.get(word).add(pageEntry);
                }
            }
        }
    }

    private Map<String, PdfDocument> returnMapPdfFromDir(File pdfsDir) throws IOException {
        Map<String, PdfDocument> pdfDocumentMap = new HashMap<>();
        File pdf;
        for (File file : pdfsDir.listFiles()) {
            if (file.getName().endsWith(".pdf")) {
                pdf = new File(pdfsDir + "/" + file.getName());
                String name = pdf.getName();
                var doc = new PdfDocument(new PdfReader(pdf));
                pdfDocumentMap.put(name, doc);
            }
        }
        return pdfDocumentMap;
    }

    @Override
    public List<PageEntry> search(String word) {

        return wordMap.get(word.toLowerCase()).stream()
                .sorted()
                .collect(Collectors.toList());
    }
}
