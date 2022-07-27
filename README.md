# Дипломная работа по курсу Java-разработчик "Поисковая система"

### Задачи:
Разработать класс поискового движка, который способен быстро находить указанное слово среди pdf-файлов, причём ранжировать результаты по количеству вхождений. Также разработать сервер, который обслуживает входящие запросы с помощью этого движка.

### Используемые технологии Java, Maven, библиотеки: itextpdf, Gson;

## Результат: 
- Обращаясь к шаблону Flyweight разработал класс [BooleanSearchEngine](https://github.com/Omen1984/diplom-nethology-search-system/blob/master/src/main/java/BooleanSearchEngine.java) который при инициализации сразу просчитывает варианты совпадений и записывает их в HashMap и теперь нам достаточно просто используя метод search для поиска по слову и получению списка совпадений (объект PageEntry)- благодаря хранению в HashMap ответ получается быстрым. 
```java
    public class BooleanSearchEngine implements SearchEngine {
      private Map<String, List<PageEntry>> wordMap;

      public BooleanSearchEngine(File pdfsDir) throws IOException {
          wordMap = new TreeMap<>();
          Map<String, PdfDocument> pdfDocumentMap = returnMapPdfFromDir(pdfsDir);
          fillingWordMap(pdfDocumentMap);
    }
```

- Разработан интерфейс IParserObjects с одним методом parse который принимает список объектов PageEntry и возвращает строку - ParserObjectsToJson его реализация. С помощью библиотеки Gson мы получаем строку в формате JSON.

```java
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
```
- Написан простой сервер в классе Main с помощью класса ServerSocket из стандартной библиотеки который принимает слово и возвращает клиенту JSON со списком PageEntry(совпадений)
- Также написан простой клиент для отправки запроса, чтобы продемонстрировать работу с выводом в консоль

