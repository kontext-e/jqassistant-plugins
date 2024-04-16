package de.kontext_e.jqassistant.plugin.cpp.scanner;

import java.io.ByteArrayInputStream;
import org.junit.Before;
import org.junit.Test;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.cpp.store.descriptor.ClassDescriptor;

import static org.mockito.Mockito.*;

public class CppAstParserTest {

    private CppAstParser cppAstParser;
    private final Store store = mock(Store.class);

    @Before
    public void setUp() {
        cppAstParser = new CppAstParser();
    }

    @Test
    public void parseEmptyStream() throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[0]);

        cppAstParser.readStream(store, is);

        verifyNoInteractions(store);
    }

    @Test
    public void parseCxxRecordDefinition() throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(
                "|-CXXRecordDecl 0x2b24038 <line:12:1, line:36:1> line:12:7 class Alice definition     "
                        .getBytes());
        ClassDescriptor mockClassDescriptor = mock(ClassDescriptor.class);
        when(store.create(ClassDescriptor.class)).thenReturn(mockClassDescriptor);

        cppAstParser.readStream(store, is);

        verify(store).create(ClassDescriptor.class);
        verify(mockClassDescriptor).setName("Alice");
    }


}
