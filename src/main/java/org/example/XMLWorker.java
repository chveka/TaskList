package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class XMLWorker {
    DocumentBuilderFactory factory;
    Document doc;
    DocumentBuilder builder;

    /**
     * Метод чтения задач из xml файла
     */
    public ArrayList<Task> xmlReader(String filePath) {
        ArrayList<Task> tasks = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            File xmlFile = new File(filePath);
            doc = builder.parse(xmlFile);
            NodeList nodeList = doc.getDocumentElement().getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node instanceof Element) {
                    Task task = new Task();
                    task.setId(Integer.parseInt(node.getAttributes().getNamedItem("id").getNodeValue()));
                    NodeList childNodes = node.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        Node cNode = childNodes.item(j);
                        if (cNode instanceof Element) {
                            String content = cNode.getLastChild().
                                    getTextContent().trim();
                            switch (cNode.getNodeName()) {
                                case "Caption":
                                    task.setCaption(content);
                                    break;
                                case "Description":
                                    task.setDescription(content);
                                    break;
                                case "Priority":
                                    task.setPriority(Integer.parseInt(content));
                                    break;
                                case "Deadline":
                                    task.setDeadline(formatter.parse(content));
                                    break;
                                case "Status":
                                    task.setStatus(content);
                                    break;
                                case "Complete":
                                    if (content.equals("null")) {
                                        task.setComplete(null);
                                    } else {
                                        task.setComplete(formatter.parse(content));
                                    }
                                    break;
                            }
                        }
                    }
                    tasks.add(task);
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return tasks;
    }

    /**
     * Метод для записи списка задач в xml файл
     */
    public void xmlWriter(ArrayList<Task> tasks, String filePath) {
        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            Element rootElement = doc.createElement("Task");
            doc.appendChild(rootElement);

            for (Task task : tasks) {
                rootElement.appendChild(getTask(doc, task));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult file = new StreamResult(new File(filePath));
            transformer.transform(source, file);
            System.out.println("Создание XML файла закончено");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод перевода объккта Task в xml вид
     */
    private static Node getTask(Document doc, Task task) {
        Element element = doc.createElement("Task");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

        element.setAttribute("id", String.valueOf(task.getId()));
        element.appendChild(getTasksElements(doc, element, "Caption", task.getCaption()));
        element.appendChild(getTasksElements(doc, element, "Description", task.getDescription()));
        element.appendChild(getTasksElements(doc, element, "Priority", String.valueOf(task.getPriority())));
        element.appendChild(getTasksElements(doc, element, "Deadline", format.format(task.getDeadline())));
        element.appendChild(getTasksElements(doc, element, "Status", String.valueOf(task.getStatus())));
        if (task.getComplete() == null) {
            element.appendChild(getTasksElements(doc, element, "Complete", String.valueOf((task.getComplete()))));
        } else {
            element.appendChild(getTasksElements(doc, element, "Complete", format.format((task.getComplete()))));
        }

        return element;
    }

    /**
     * Метод для перевода каждого параметра задачи в xml вид
     */
    private static Node getTasksElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
}
