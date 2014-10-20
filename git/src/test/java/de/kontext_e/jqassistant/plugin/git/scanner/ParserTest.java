package de.kontext_e.jqassistant.plugin.git.scanner;

import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() throws Exception {
        parser = new Parser();
    }

    @Test
    public void testRecognizeSha() {
        final List<GitCommit> parseResult = parser.parse(asList(
                "commit 32a16fc9f93a6696112b07e9aeb8771a9388ce22"
        ));

        assertThat(parseResult.size(), is(1));
        assertThat(parseResult.get(0).getSha(), is("32a16fc9f93a6696112b07e9aeb8771a9388ce22"));
    }

    @Test
    public void testRecognizeAuthor() {
        final List<GitCommit> parseResult = parser.parse(asList(
                "commit 32a16fc9f93a6696112b07e9aeb8771a9388ce22",
                "Author: jn <j.nerche@kontext-e.de>"
        ));

        assertThat(parseResult.size(), is(1));
        assertThat(parseResult.get(0).getAuthor(), is("jn <j.nerche@kontext-e.de>"));
    }

    @Test
    public void testRecognizeDate() {
        final List<GitCommit> parseResult = parser.parse(asList(
                "commit 32a16fc9f93a6696112b07e9aeb8771a9388ce22",
                "Date:   2014-03-21 15:03:44 +0200"
        ));

        assertThat(parseResult.size(), is(1));
        assertThat(parseResult.get(0).getDate(), is("2014-03-21 15:03:44 +0200"));
    }

    @Test
    public void testRecognizeComment() {
        final List<GitCommit> parseResult = parser.parse(asList(
                "commit 32a16fc9f93a6696112b07e9aeb8771a9388ce22",
                "    Merge branch 'develop' of something into develop",
                "",
                " ",
                "\t",
                "    Conflicts:",
                "    \tpackage1/ClassA.java",
                "    \tpackage2/ClassB.java"
        ));

        assertThat(parseResult.size(), is(1));
        assertThat(parseResult.get(0).getMessage().size(), is(4));
        assertThat(parseResult.get(0).getMessage().get(0), is("Merge branch 'develop' of something into develop"));
        assertThat(parseResult.get(0).getMessage().get(1), is("Conflicts:"));
        assertThat(parseResult.get(0).getMessage().get(2), is("package1/ClassA.java"));
        assertThat(parseResult.get(0).getMessage().get(3), is("package2/ClassB.java"));
    }

    @Test
    public void testRecognizeCommitFile() {
        final List<GitCommit> parseResult = parser.parse(asList(
                "commit 32a16fc9f93a6696112b07e9aeb8771a9388ce22",
                "A   package1/ClassA.java",
                "M   package1/ClassB.java",
                "D   package1/ClassC.java"
        ));

        assertThat(parseResult.size(), is(1));
        assertThat(parseResult.get(0).getCommitFiles().size(), is(3));
        assertThat(parseResult.get(0).getCommitFiles().get(0).getModificationKind(), is("A"));
        assertThat(parseResult.get(0).getCommitFiles().get(0).getRelativePath(), is("package1/ClassA.java"));
        assertThat(parseResult.get(0).getCommitFiles().get(1).getModificationKind(), is("M"));
        assertThat(parseResult.get(0).getCommitFiles().get(1).getRelativePath(), is("package1/ClassB.java"));
        assertThat(parseResult.get(0).getCommitFiles().get(2).getModificationKind(), is("D"));
        assertThat(parseResult.get(0).getCommitFiles().get(2).getRelativePath(), is("package1/ClassC.java"));
    }

    @Test
    public void testRecognizeSecondCommit() {
        final List<GitCommit> parseResult = parser.parse(asList(
                "commit 32a16fc9f93a6696112b07e9aeb8771a9388ce22",
                "Author: jn <j.nerche@kontext-e.de>",
                "commit 18896e8631fbcec395a4e3e8a7e30a7687895799",
                "Author: Dirk Mahler <dirk.mahler@buschmais.com>"
        ));

        assertThat(parseResult.size(), is(2));
        assertThat(parseResult.get(0).getAuthor(), is("jn <j.nerche@kontext-e.de>"));
        assertThat(parseResult.get(1).getAuthor(), is("Dirk Mahler <dirk.mahler@buschmais.com>"));
    }

    @Test
    public void testNoCommit() {
        final List<GitCommit> parseResult = parser.parse(asList(
                "Author: Dirk Mahler <dirk.mahler@buschmais.com>"
        ));

        assertThat(parseResult.size(), is(0));
    }

}
