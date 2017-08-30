package ru.xpendence.test.fileservice;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class working with XML files.
 * Created by promoscow on 19.08.17.
 */
public class FileServiceImpl implements FileService {

    private static final String RESOURCES_DIRECTORY = "./src/main/resources/";
    private static final Logger logger = Logger.getLogger(FileServiceImpl.class.getName());

    private DocumentBuilderFactory factory;

    public FileServiceImpl() {
        this.factory = DocumentBuilderFactory.newInstance();
    }

    @Override
    public void create(List<Integer> list) {
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement("entries");
            document.appendChild(root);

            for (Integer value : list) {
                Element subRoot = document.createElement("entry");
                root.appendChild(subRoot);

                Element entry = document.createElement("field");
                entry.setTextContent(String.valueOf(value));
                subRoot.appendChild(entry);
            }
            StreamResult file = new StreamResult(new File(RESOURCES_DIRECTORY + "1.xml"));
            DOMSource source = new DOMSource(document);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(source, file);

        } catch (ParserConfigurationException | TransformerException e) {
            logger.log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void transform(String impl) {
        try {
            Transformer transformer = TransformerFactory
                    .newInstance()
                    .newTransformer(new StreamSource(new File(RESOURCES_DIRECTORY
                            + ("correct".equals(impl) ? "templateCorrect.xsl"
                            : "tech.task".equals(impl) ? "templateOriginal.xsl" : null))));

            StreamSource parent = new StreamSource(new File(RESOURCES_DIRECTORY + "1.xml"));
            StreamResult child = new StreamResult(new File(RESOURCES_DIRECTORY + "2.xml"));
            transformer.transform(parent, child);

        } catch (TransformerException e) {
            logger.log(Level.SEVERE, null, e);
        }
    }

    @Override
    public int read() {
        int result = 0;

        DocumentBuilder builder;
        Document document;

        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(RESOURCES_DIRECTORY + "2.xml");

            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile("//entries/entry[@field]");
            NodeList nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node field = nodeList.item(i);
                String key = field.getAttributes().getNamedItem("field").getNodeValue();
                result += Integer.parseInt(key);
            }
        } catch (SAXException | IOException | ParserConfigurationException | XPathExpressionException e) {
            logger.log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public int readAlt() {
        int result = 0;
        int indent = 18;
        String fileName = RESOURCES_DIRECTORY + "2.xml";

        try (BufferedReader bufferedReader
                     = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += (line.contains("field="))
                        ? Integer.parseInt(line.substring(indent, line.length() - 2)) : 0;
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, null, e);
        }
        return result;
    }
}
