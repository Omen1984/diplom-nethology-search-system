import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {

    private static final int PORT = 8989;

    public static void main(String[] args) throws IOException {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Start server port: " + PORT + "...");
            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream());
                ) {
                    IParserObjects parser = new ParserObjectsToJson();
                    final String word = in.readLine();

                    List<PageEntry> wordList = engine.search(word);

                    String result = parser.parse(wordList);
                    out.println(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}