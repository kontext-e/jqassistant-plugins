package de.kontext_e.jqassistant.plugin.pmd.scanner;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import de.kontext_e.jqassistant.plugin.pmd.store.PmdFileDescriptor;
import de.kontext_e.jqassistant.plugin.pmd.store.PmdReportDescriptor;
import de.kontext_e.jqassistant.plugin.pmd.store.PmdViolationDescriptor;


/**
 * @author aw, Kontext E GmbH, 29.01.15
 */
@ScannerPlugin.Requires(FileDescriptor.class)
public class PmdReportScannerPlugin extends AbstractScannerPlugin<FileResource, PmdReportDescriptor> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmdReportScannerPlugin.class);
    public static final String JQASSISTANT_PLUGIN_PMD_FILENAME = "jqassistant.plugin.pmd.filename";
    public static final String JQASSISTANT_PLUGIN_PMD_DIRNAME = "jqassistant.plugin.pmd.dirname";

    private String pmdFileName = "pmd.xml";
    private String pmdDirName = "pmd";

    @Override
    protected void configure() {
        super.configure();

        if(getProperties().containsKey(JQASSISTANT_PLUGIN_PMD_FILENAME)) {
            pmdFileName = (String) getProperties().get(JQASSISTANT_PLUGIN_PMD_FILENAME);
        }
        if(System.getProperty(JQASSISTANT_PLUGIN_PMD_FILENAME) != null) {
            pmdFileName = System.getProperty(JQASSISTANT_PLUGIN_PMD_FILENAME);
        }
        if(getProperties().containsKey(JQASSISTANT_PLUGIN_PMD_DIRNAME)) {
            pmdDirName = (String) getProperties().get(JQASSISTANT_PLUGIN_PMD_DIRNAME);
        }
        if(System.getProperty(JQASSISTANT_PLUGIN_PMD_DIRNAME) != null) {
            pmdDirName = System.getProperty(JQASSISTANT_PLUGIN_PMD_DIRNAME);
        }
        LOGGER.info(String.format("PMD plugin looks for files named %s or for all XML files in directories named '%s'", pmdFileName, pmdDirName));
    }

    @Override
    public boolean accepts(FileResource item, String path, Scope scope) throws IOException {
        try {
            boolean accepted = path.endsWith(pmdFileName) || (pmdDirName.equals(item.getFile().toPath().getParent().toFile().getName()) && path.endsWith(".xml"));
            if(accepted) {
                LOGGER.info("Pmd accepted path "+path);
            }
            return accepted;
        } catch (NullPointerException e) {
            // could do a lengthy null check at beginning or do it the short dirty way
            return false;
        } catch (Exception e) {
            LOGGER.error("Error while checking path: "+e, e);
            return false;
        }
    }

    @Override
    public PmdReportDescriptor scan(final FileResource file, String path, Scope scope, Scanner scanner) {
        try {
            FileDescriptor fileDescriptor = scanner.getContext().getCurrentDescriptor();
            final PmdReportDescriptor pmdReportDescriptor = scanner.getContext().getStore().addDescriptorType(fileDescriptor, PmdReportDescriptor.class);

            scanViaDom(file.createStream(), pmdReportDescriptor, scanner.getContext().getStore());
            return pmdReportDescriptor;
        } catch (Exception e) {
            LOGGER.warn("Error while scanning a PMD file: "+e, e);
            return null;
        }
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
        if(child == null) return;
        if(attributeConumser == null) return;

        NamedNodeMap attributes = child.getAttributes();
        if(attributes == null) return;
        for(int a = 0; a < attributes.getLength(); a++) {
            Node attr = attributes.item(a);
            if(attr == null) continue;
            attributeConumser.accept(attr);
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
        if(!"file".equalsIgnoreCase(fileNode.getNodeName())) return;

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
        if(!"violation".equalsIgnoreCase(fileNode.getNodeName())) return;

        final PmdViolationDescriptor vioDescriptor = store.create(PmdViolationDescriptor.class);
        pmdFileDescriptor.getViolations().add(vioDescriptor);
        final String[] x = new String[2];
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
                if(x[0] == null) {
                    x[0] = node.getNodeValue();
                }
                vioDescriptor.setPackage(node.getNodeValue());
            }
            if("class".equalsIgnoreCase(node.getNodeName())) {
                if(x[1] == null) {
                    x[1] = node.getNodeValue();
                }
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
        pmdFileDescriptor.setFullQualifiedName(x[0]+"."+x[1]);

        visitNode(fileNode, node -> {
            if("#text".equalsIgnoreCase(node.getNodeName())) {
                String message = node.getNodeValue();
                message = message.replaceAll("\\n","").trim();
                vioDescriptor.setMessage(message);
            }
        });
    }
}
