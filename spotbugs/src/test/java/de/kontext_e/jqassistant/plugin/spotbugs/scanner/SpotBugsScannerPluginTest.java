package de.kontext_e.jqassistant.plugin.spotbugs.scanner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.Test;

import de.kontext_e.jqassistant.plugin.spotbugs.jaxb.BugCollectionType;
import de.kontext_e.jqassistant.plugin.spotbugs.jaxb.BugInstanceType;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class SpotBugsScannerPluginTest {
	private SpotBugsScannerPlugin plugin = new SpotBugsScannerPlugin();

	@Test
	public void spotbugs() throws Exception {
		final String xml = "<?xml version='1.0' encoding='UTF-8'?>\n" +
						   "<BugCollection version='3.1.3' threshold='medium' effort='default'>\n" +
						   "    <file classname='com.example.LogFormatter'>\n" +
						   "        <BugInstance type='DE_MIGHT_IGNORE' priority='Normal' category='BAD_PRACTICE' message='com.example.LogFormatter.format(LogRecord) might ignore java.lang.Exception' lineNumber='31'/>\n" +
						   "    </file>\n" +
						   "    <file classname='com.example.Main'>\n" +
						   "        <BugInstance type='LG_LOST_LOGGER_DUE_TO_WEAK_REFERENCE' priority='High' category='EXPERIMENTAL' message='Changes to logger could be lost in com.example.Main.initializeLogging()' lineNumber='91'/>\n" +
						   "        <BugInstance type='MS_CANNOT_BE_FINAL' priority='Normal' category='MALICIOUS_CODE' message='com.example.Main.primaryStage isn&apos;t final and can&apos;t be protected from malicious code ' lineNumber='-1'/>\n" +
						   "        <BugInstance type='ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD' priority='Normal' category='STYLE' message='Write to static field com.example.Main.primaryStage from instance method com.example.Main.start(Stage)' lineNumber='75'/>\n" +
						   "    </file>\n"+
						   "    <Error></Error>\n" +
						   "    <Project>\n" +
						   "        <SrcDir>src\\main\\java</SrcDir>\n" +
						   "        <SrcDir>src\\test\\java</SrcDir>\n" +
						   "    </Project>\n"+
						   "</BugCollection>\n"
				;
		final InputStream input = new ByteArrayInputStream(xml.getBytes());

		final BugCollectionType bugCollectionType = plugin.unmarshalSpotBugsXml(input);

		assertThat(bugCollectionType.getFile().size(), is(2));
		assertThat(bugCollectionType.getFile().get(0).getClassname(), is("com.example.LogFormatter"));
		BugInstanceType firstBug = bugCollectionType.getFile().get(0).getBugInstance().get(0);
		assertThat(firstBug.getType(), is("DE_MIGHT_IGNORE"));
		assertThat(firstBug.getLineNumber(), is(31));
	}

}
