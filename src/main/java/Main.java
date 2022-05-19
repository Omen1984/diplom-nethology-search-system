import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {

    private static final int PORT = 8989;

    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Start server port: " + PORT + "...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            IParserObjects parser = new ParserObjectsToJson();

            final String word = in.readLine();

            List<PageEntry> wordList = engine.search(word);

            String result = parser.parse(wordList);
            out.println(result);
        }
    }
}