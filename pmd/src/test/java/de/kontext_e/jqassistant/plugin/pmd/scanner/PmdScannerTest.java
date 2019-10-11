package de.kontext_e.jqassistant.plugin.pmd.scanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.pmd.store.PmdFileDescriptor;
import de.kontext_e.jqassistant.plugin.pmd.store.PmdReportDescriptor;
import de.kontext_e.jqassistant.plugin.pmd.store.PmdViolationDescriptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PmdScannerTest {

    @Test
    public void dom() throws Exception {
        final File file = new File("src/test/main.xml");
        final InputStream is = new FileInputStream(file);
        final PmdReportDescriptor pmdReportDescriptor = mock(PmdReportDescriptor.class);
        final Store store = mock(Store.class);
        PmdFileDescriptor pmdFileDescriptor = mock(PmdFileDescriptor.class);
        PmdViolationDescriptor pmdViolationDescriptor = mock(PmdViolationDescriptor.class);
        when(store.create(PmdFileDescriptor.class)).thenReturn(pmdFileDescriptor);
        when(store.create(PmdViolationDescriptor.class)).thenReturn(pmdViolationDescriptor);


        scanViaDom(is, pmdReportDescriptor, store);

        verify(pmdReportDescriptor).setVersion("6.10.0");
        verify(pmdReportDescriptor).setTimestamp("2018-12-18T13:17:50.505");

        verify(pmdFileDescriptor).setName("C:\\Users\\jn\\projects\\jqassistant-plugins\\pmd\\src\\main\\java\\de\\kontext_e\\jqassistant\\plugin\\pmd\\jaxb\\FileType.java");
    }

    private void scanViaDom(final InputStream is, final PmdReportDescriptor pmdReportDescriptor, final Store store) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(is);
        Node rootNode = doc.getDocumentElement();

        attributes(rootNode, node -> {
            if("version".equalsIgnoreCase(node.getNodeName())) {
                pmdReportDescriptor.setVersion(node.getNodeValue());
            }
            if("timestamp".equalsIgnoreCase(node.getNodeName())) {
                pmdReportDescriptor.setTimestamp(node.getNodeValue());
            }
        });
        visitNode(rootNode, node -> importFileNode(node, store, pmdReportDescriptor));
    }

    private void attributes(final Node child, Consumer<Node> attributeConumser) {
        try {
            NamedNodeMap attributes = child.getAttributes();
            for(int a = 0; a < attributes.getLength(); a++) {
                Node attr = attributes.item(a);
                attributeConumser.accept(attr);
            }
        } catch (Exception e) {

        }
    }

    private void visitNode(Node node, Consumer<Node> nodeConsumer) {
        NodeList childNodes = node.getChildNodes();
        for(int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if("#comment".equalsIgnoreCase(child.getNodeName())) continue;
            nodeConsumer.accept(child);
        }
    }

    private void importFileNode(final Node fileNode, final Store store, final PmdReportDescriptor pmdReportDescriptor) {
        final PmdFileDescriptor pmdFileDescriptor = store.create(PmdFileDescriptor.class);
        pmdReportDescriptor.getFiles().add(pmdFileDescriptor);
        attributes(fileNode, node -> {
            if("name".equalsIgnoreCase(node.getNodeName())) {
                pmdFileDescriptor.setName(node.getNodeValue());
            }
        });
        visitNode(fileNode, node -> importViolationNode(node, store, pmdFileDescriptor));
    }

    private void importViolationNode(final Node fileNode, final Store store, final PmdFileDescriptor pmdFileDescriptor) {
        final PmdViolationDescriptor vioDescriptor = store.create(PmdViolationDescriptor.class);
        pmdFileDescriptor.getViolations().add(vioDescriptor);
        attributes(fileNode, node -> {
            if("beginline".equalsIgnoreCase(node.getNodeName())) {
                vioDescriptor.setBeginLine(Integer.valueOf(node.getNodeValue()));
            }
            if("endline".equalsIgnoreCase(node.getNodeName())) {
                vioDescriptor.setEndLine(Integer.valueOf(node.getNodeValue()));
            }
            if("begincolumn".equalsIgnoreCase(node.getNodeName())) {
                vioDescriptor.setBeginColumn(Integer.valueOf(node.getNodeValue()));
            }
            if("endcolumn".equalsIgnoreCase(node.getNodeName())) {
                vioDescriptor.setEndColumn(Integer.valueOf(node.getNodeValue()));
            }
            if("rule".equalsIgnoreCase(node.getNodeName())) {
                vioDescriptor.setRule(node.getNodeValue());
            }
            if("ruleset".equalsIgnoreCase(node.getNodeName())) {
                vioDescriptor.setRuleSet(node.getNodeValue());
            }
            if("package".equalsIgnoreCase(node.getNodeName())) {
                vioDescriptor.setPackage(node.getNodeValue());
            }
            if("class".equalsIgnoreCase(node.getNodeName())) {
                vioDescriptor.setClassName(node.getNodeValue());
            }
            if("externalInfoUrl".equalsIgnoreCase(node.getNodeName())) {
                vioDescriptor.setExternalInfoUrl(node.getNodeValue());
            }
            if("priority".equalsIgnoreCase(node.getNodeName())) {
                vioDescriptor.setPriority(Integer.valueOf(node.getNodeValue()));
            }
            if("variable".equalsIgnoreCase(node.getNodeName())) {
                vioDescriptor.setVariable(node.getNodeValue());
            }
            if("method".equalsIgnoreCase(node.getNodeName())) {
                vioDescriptor.setMethod(node.getNodeValue());
            }
        });
        visitNode(fileNode, node -> {
            if("#text".equalsIgnoreCase(node.getNodeName())) {
                vioDescriptor.setMessage(node.getNodeValue());
            }
        });
    }
}
