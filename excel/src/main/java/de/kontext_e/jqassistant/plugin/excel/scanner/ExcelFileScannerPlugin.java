package de.kontext_e.jqassistant.plugin.excel.scanner;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import de.kontext_e.jqassistant.plugin.excel.store.descriptor.ExcelFileDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.Arrays.asList;

@ScannerPlugin.Requires(FileDescriptor.class)
public class ExcelFileScannerPlugin extends AbstractScannerPlugin<FileResource, ExcelFileDescriptor> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelFileScannerPlugin.class);
    private static final List<String> suffixes = asList("xls", "xlsx");

    @Override
    public boolean accepts(FileResource item, String path, Scope scope) {
        try {
            int beginIndex = path.lastIndexOf(".");
            if(beginIndex > 0) {
                final String suffix = path.substring(beginIndex + 1).toLowerCase();

                boolean accepted = suffixes.contains(suffix);
                if(accepted) {
                    LOGGER.info("ExcelFileScannerPlugin accepted path "+path);
                }

                return accepted;
            }

            return false;
        } catch (Exception e) {
            LOGGER.error("Error while checking path: "+e, e);
            return false;
        }
    }

    @Override
    public ExcelFileDescriptor scan(FileResource item, String path, Scope scope, Scanner scanner) {
        try {
            final Store store = scanner.getContext().getStore();
            final FileDescriptor fileDescriptor = scanner.getContext().getCurrentDescriptor();
            final ExcelFileDescriptor excelFileDescriptor = store.addDescriptorType(fileDescriptor, ExcelFileDescriptor.class);

            final ExcelFileReader excelFileReader = new ExcelFileReader(store, excelFileDescriptor, item.createStream());
            excelFileReader.read();

            return excelFileDescriptor;
        } catch (Exception e) {
            LOGGER.error("Error while scanning "+path+": "+e);
            LOGGER.debug("Details:", e);
            return null;
        }
    }
}
