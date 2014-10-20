package de.kontext_e.jqassistant.plugin.git.scanner;

import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.VirtualFile;
import com.buschmais.jqassistant.plugin.common.impl.scanner.AbstractScannerPlugin;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitDescriptor;

/**
 * @author jn4, Kontext E GmbH
 */
public class GitScannerPlugin extends AbstractScannerPlugin<VirtualFile> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitScannerPlugin.class);

    private String gitConfigurationFileName = "jqa_plugin_git.properties";

    @Override
    public boolean accepts(final VirtualFile item, final String path, final Scope scope) throws IOException {
        return path.endsWith(gitConfigurationFileName);
    }

    @Override
    public FileDescriptor scan(final VirtualFile item, final String path, final Scope scope, final Scanner scanner) throws IOException {
        Properties configuration = new Properties();
        configuration.load(item.createStream());

        final GitDescriptor gitDescriptor = scanner.getContext().getStore().create(GitDescriptor.class);
        gitDescriptor.setName(path);
        gitDescriptor.setFileName(path);

        return gitDescriptor;
    }

    @Override
    protected void initialize() {
        super.initialize();
        final String property = (String) getProperties().get("jqassistant.plugin.git.filename");
        if(property != null) {
            gitConfigurationFileName = property;
        }
    }
}
