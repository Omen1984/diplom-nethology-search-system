import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ParserObjectsToJson implements IParserObjects {

    @Override
    public String parse(List<PageEntry> wordList) {
        Type listType = new TypeToken<List<PageEntry>>() {
        }.getType();

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        return gson.toJson(wordList, listType);
    }
}
