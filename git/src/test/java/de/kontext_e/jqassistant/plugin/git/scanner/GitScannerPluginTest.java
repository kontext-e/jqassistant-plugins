package de.kontext_e.jqassistant.plugin.git.scanner;

import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitDescriptor;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test cases for GitScannerPlugin
 *
 * Currently this is restricted to the business logic (for
 *
 * @author Gerd Aschemann - gerd@aschemann.net - @GerdAschemann
 * @since 1.1.0
 */

public class GitScannerPluginTest {

    @Test
    public void testGitScannerInitGitDescriptorDefault () {
        GitDescriptor gitDescriptor = mock(GitDescriptor.class);
        try {
            GitScannerPlugin.initGitDescriptor(gitDescriptor, new File("/tmp/xxx/.git/HEAD"));
            verify(gitDescriptor).setFileName("/tmp/xxx/.git");
            verify(gitDescriptor).setName("xxx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGitScannerInitGitDescriptorMyOwnGit () {
        GitDescriptor gitDescriptor = mock(GitDescriptor.class);
        try {
            GitScannerPlugin.initGitDescriptor(gitDescriptor, new File("../.git/HEAD"));
            // Cannot do any verifications since the plugins project may be cloned under any name
            // But check the debug output if you would like to make sure it works
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGitScannerInitGitDescriptorRelativeDotGit () {
        GitDescriptor gitDescriptor = mock(GitDescriptor.class);
        try {
            GitScannerPlugin.initGitDescriptor(gitDescriptor, new File(".git/HEAD"));
            // Expected to be run in .../git/build
            verify(gitDescriptor).setName("git");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGitScannerInitGitDescriptorRelativeDotDotGit () {
        GitDescriptor gitDescriptor = mock(GitDescriptor.class);
        try {
            GitScannerPlugin.initGitDescriptor(gitDescriptor, new File("./.git/HEAD"));
            // Expected to be run in .../git/build
            verify(gitDescriptor).setName("git");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
