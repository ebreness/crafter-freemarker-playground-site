import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

public class ArticleGenerator {
    private static final int NUM_FILES = 10000; // Number of XML files to generate

    public static void main(String[] args) {
        try {
            // Create a DocumentBuilder
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            for (int i = 1; i <= NUM_FILES; i++) {
                System.out.println("generating article # " + i);
                // Create a new XML document
                Document doc = docBuilder.newDocument();

                // Create the root element <page>
                Element pageElement = doc.createElement("page");
                doc.appendChild(pageElement);

                // Set the XML version and encoding
                doc.setXmlVersion("1.0");
                doc.setXmlStandalone(true);
                pageElement.setAttribute("encoding", "UTF-8");

                // Add child elements with their corresponding values
                addElementWithValue(doc, pageElement, "content-type", "/page/article");
                addElementWithValue(doc, pageElement, "display-template", "/templates/web/article.ftl");
                pageElement.appendChild(doc.createElement("no-template-required"));
                addElementWithValue(doc, pageElement, "merge-strategy", "inherit-levels");
                addElementWithValue(doc, pageElement, "objectGroupId", generateRandomNumber(4));
                addElementWithValue(doc, pageElement, "objectId", generateUUID());
                addElementWithValue(doc, pageElement, "internal-name", "article " + i);
                addElementWithValue(doc, pageElement, "navLabel", "article " + i);
                addElementWithValue(doc, pageElement, "subtitle_s", generateRandomSentence());
                addElementWithValue(doc, pageElement, "author_s", generateRandomSentence());
                addElementWithValue(doc, pageElement, "readTime_s", generateRandomNumber(2) + " minutes");
                pageElement.appendChild(doc.createElement("placeInNav"));
                addElementWithValue(doc, pageElement, "orderDefault_f", "-1");
                addElementWithValue(doc, pageElement, "file-name", "index.xml");
                addElementWithValue(doc, pageElement, "folder-name", "article " + i);
                addElementWithValue(doc, pageElement, "content_html", generateRandomHTML());
                addElementWithValue(doc, pageElement, "createdDate", generateRandomDate());
                addElementWithValue(doc, pageElement, "createdDate_dt", generateRandomDate());
                addElementWithValue(doc, pageElement, "lastModifiedDate", generateRandomDate());
                addElementWithValue(doc, pageElement, "lastModifiedDate_dt", generateRandomDate());

                // Generate the XML file
                String fileName = "index.xml";
                String folderName = "article-" + i;
                String folderPath = "site" + File.separator + "website" + File.separator + folderName;
                File folder = new File(folderPath);
                folder.mkdirs(); // Create the folder

                String filePath = folderPath + File.separator + fileName;
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new FileOutputStream(new File(filePath)));
                transformer.transform(source, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addElementWithValue(Document doc, Element parentElement, String tagName, String value) {
        Element element = doc.createElement(tagName);
        Text text = doc.createTextNode(value);
        element.appendChild(text);
        parentElement.appendChild(element);
    }

    private static String generateRandomNumber(int digitCount) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < digitCount; i++) {
            int digit = random.nextInt(10);
            builder.append(digit);
        }
        return builder.toString();
    }

    private static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    private static String generateRandomSentence() {
        StringBuilder sentenceBuilder = new StringBuilder();
        Random random = new Random();

        // Generate at least 1000 words
        while (sentenceBuilder.length() < 10) {
            // Generate a random word of 5-10 characters
            int wordLength = random.nextInt(6) + 5;
            String word = generateRandomWord(wordLength);
            sentenceBuilder.append(word).append(" ");
        }

        return sentenceBuilder.toString().trim();
    }

    private static String generateRandomHTML() {
        StringBuilder htmlBuilder = new StringBuilder();
        Random random = new Random();

        // Generate at least 10000 words
        while (htmlBuilder.length() < 10000) {
            // Generate a random word of 5-10 characters
            int wordLength = random.nextInt(6) + 5;
            String word = generateRandomWord(wordLength);
            htmlBuilder.append(word).append(" ");
        }

        return htmlBuilder.toString().trim();
    }

    private static String generateRandomWord(int length) {
        StringBuilder wordBuilder = new StringBuilder();
        Random random = new Random();

        // Generate a random word of the specified length
        for (int i = 0; i < length; i++) {
            char c = (char) (random.nextInt(26) + 'a');
            wordBuilder.append(c);
        }

        return wordBuilder.toString();
    }

    private static String generateRandomDate() {
        // Generate a random date in the format yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime randomDateTime = now.minusDays(new Random().nextInt(365));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return randomDateTime.format(formatter);
    }
}
