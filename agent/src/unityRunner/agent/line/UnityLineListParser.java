package unityRunner.agent.line;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: terry.drever
 * Date: 20/03/2013
 * Time: 12:22
 * To change this template use File | Settings | File Templates.
 */
public class UnityLineListParser {
    public static List<Line> lines = new ArrayList<Line>();

    public static void ParseLines(java.io.File lineListFile) {
        if(!lineListFile.exists())
        {
            lines = Arrays.asList(
                    // Infos
                    new Line("There are inconsistent line endings in the.*?", Line.Type.Normal),
                    new Line("This might lead to incorrect line numbers in stacktraces and compiler errors.*?", Line.Type.Normal),

                    // Warnings
                    new Line("Script attached to.*?is missing or no valid script is attached.", Line.Type.Warning),
                    new Line(".*?warning CS\\d+.*?", Line.Type.Warning),
                    new Line("WARNING.*", Line.Type.Warning),

                    // Errors
                    new Line(".*?error CS\\d+.*?", Line.Type.Error),
                    new Line("Compilation failed:.*", Line.Type.Error),
                    new Line("Scripts have compiler errors\\..*", Line.Type.Error),
                    new Line("\\w+Exception.*", Line.Type.Error),
                    new Line("ERROR.*", Line.Type.Error));

            return;
        }

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(lineListFile);

            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList lineList = doc.getElementsByTagName("line");
            for (int lineListIndex = 0; lineListIndex < lineList.getLength(); lineListIndex++) {
                Node lineListNode = lineList.item(lineListIndex);

                if (lineListNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element lineListElement = (Element)lineListNode;
                    String level = lineListElement.getAttribute("level");
                    String message = lineListElement.getAttribute("message");

                    Line.Type lineType = Line.Type.Normal;
                    if(level.equals("normal")) {
                        lineType = Line.Type.Normal;
                    }
                    else if(level.equals("warning")) {
                        lineType = Line.Type.Warning;
                    }
                    else if(level.equals("error")) {
                        lineType = Line.Type.Error;
                    }

                    lines.add(new Line(message, lineType));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
