package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.EntityFactory;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.core.Diagram;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class PumlTest {

    @Test
    public void test() {
        String plantuml2 = "[plantuml,role=concept]\n" +
                           "----\n" +
                           "@startuml\n" +
                           "[artifactId:xo.impl] as impl <<:Maven:Project>>\n" +
                           "[artifactId:xo.api] as api <<:Maven:Project>>\n" +
                           "[artifactId:xo.spi] as spi <<:Maven:Project>>\n" +
                           "\n" +
                           "impl -> api : Defines Dependency\n" +
                           "impl -> spi : Defines Dependency\n" +
                           "spi -> api : Defines Dependency\n" +
                           "@enduml\n"+
                           "----\n"
                ;

        String plantuml =
                "@startuml\n"+
                "package de.kontext_e.project.domain #ffffff {\n" +
                "}\n" +
                "package de.kontext_e.project.services #ffffff {\n" +
                "}\n" +
                "\n" +
                "de.kontext_e.project.services --> de.kontext_e.project.domain\n"+
                "@enduml\n"
                ;

        String nested =
                "@startuml\n"+
                "package de.kontext_e.project.domain #ffffff {\n" +
                "    package de.kontext_e.project.domain.sub1 {}\n" +
                "}\n" +
                "package de.kontext_e.project.services #ffffff {\n" +
                "}\n" +
                "\n" +
                "de.kontext_e.project.services --> de.kontext_e.project.domain\n"+
                "@enduml\n"
                ;


        SourceStringReader reader = new SourceStringReader(nested);
        List<BlockUml> blocks = reader.getBlocks();
        Diagram diagram = blocks.get(0).getDiagram();
        AbstractEntityDiagram descriptionDiagram = (AbstractEntityDiagram) diagram;
        EntityFactory entityFactory = descriptionDiagram.getEntityFactory();
        Collection<Entity> groups = entityFactory.groups();
        assertThat(groups.size()).isEqualTo(3);
        assertThat(groups.stream().map(g->g.getQuark().getQualifiedName()))
                .containsAll(List.of("de.kontext_e.project.domain",
                "de.kontext_e.project.domain.sub1",
                "de.kontext_e.project.services"));

        List<Link> links = entityFactory.getLinks();
        assertThat(links.size()).isEqualTo(1);
        assertThat(links.get(0).getEntity1().getQuark().getQualifiedName()).isEqualTo("de.kontext_e.project.services");
        assertThat(links.get(0).getEntity2().getQuark().getQualifiedName()).isEqualTo("de.kontext_e.project.domain");
    }

}
