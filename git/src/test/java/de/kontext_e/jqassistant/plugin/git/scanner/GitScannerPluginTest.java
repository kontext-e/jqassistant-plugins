package de.kontext_e.jqassistant.plugin.git.scanner;

import java.io.File;
import java.io.IOException;
import org.junit.Test;

import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitRepositoryDescriptor;

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
    private static final boolean IS_WINDOWS = isWindows();

    private static final boolean isWindows () {
        // TODO: This is really ugly, isn't it?
        final String osName = System.getProperty("os.name");
        return null != osName && osName.startsWith("Windows");
    }

    @Test
    public void testGitScannerInitGitDescriptorDefault () {
        GitRepositoryDescriptor gitRepositoryDescriptor = mock(GitRepositoryDescriptor.class);
        try {
            GitScannerPlugin.initGitDescriptor(gitRepositoryDescriptor, new File("/tmp/xxx/.git/HEAD"));
            if (!IS_WINDOWS) {
                verify(gitRepositoryDescriptor).setFileName("/tmp/xxx/.git");
            }
            verify(gitRepositoryDescriptor).setName("xxx");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGitScannerInitGitDescriptorMyOwnGit () {
        GitRepositoryDescriptor gitRepositoryDescriptor = mock(GitRepositoryDescriptor.class);
        try {
            GitScannerPlugin.initGitDescriptor(gitRepositoryDescriptor, new File("../.git/HEAD"));
            // Cannot do any verifications since the plugins project may be cloned under any name
            // But check the debug output if you would like to make sure it works
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGitScannerInitGitDescriptorRelativeDotGit () {
        GitRepositoryDescriptor gitRepositoryDescriptor = mock(GitRepositoryDescriptor.class);
        try {
            GitScannerPlugin.initGitDescriptor(gitRepositoryDescriptor, new File(".git/HEAD"));
            // Expected to be run in .../git/build
            verify(gitRepositoryDescriptor).setName("git");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGitScannerInitGitDescriptorRelativeDotDotGit () {
        GitRepositoryDescriptor gitRepositoryDescriptor = mock(GitRepositoryDescriptor.class);
        try {
            GitScannerPlugin.initGitDescriptor(gitRepositoryDescriptor, new File("./.git/HEAD"));
            // Expected to be run in .../git/build
            verify(gitRepositoryDescriptor).setName("git");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
