#!/usr/bin/env groovy
@Grab ('org.ajoberstar:grgit')
@Grab ('org.slf4j:slf4j-simple')

import org.ajoberstar.grgit.Grgit
import org.ajoberstar.grgit.Branch
import org.ajoberstar.grgit.Tag
import org.ajoberstar.grgit.operation.MergeOp

File createFile (File dir, String name, String contents) {
    File file = new File(dir, name)
    file.withPrintWriter { Writer writer ->
        writer.write(contents)
    }

    return file
}

File createPom (File dir, String version) {
    return createFile (dir, "pom.xml", """
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>dummy.jqassistant-plugins.samples</groupId>
    <artifactId>jqassistant-plugins-git-sample1</artifactId>
    <version>${version}</version>
    <packaging>pom</packaging>
</project>
""")
}

void deleteTree (File f) {
    if (f.directory) {
        f.eachFile {
            deleteTree(it)
        }
    }
    f.delete()
}

File tmpDir = new File(System.props['java.io.tmpdir'])
File samplesBase = new File (tmpDir, "jqassistant-plugins")
File sampleDir = new File (samplesBase, "/git/sample1")
if (sampleDir.exists()) {
    deleteTree (sampleDir)
}
assert !sampleDir.exists()
File sampleRemoteDir = new File (sampleDir, "remote")
sampleRemoteDir.mkdirs()

Grgit grgit = Grgit.init (dir: sampleRemoteDir.absolutePath)

createPom (sampleRemoteDir, "1.0.0-SNAPSHOT")
grgit.add (patterns: ['pom.xml'])
grgit.commit (message: "Starting Sample1 project")

Branch develop = grgit.checkout (branch: 'develop', createBranch: true)
createFile(sampleRemoteDir, '.gitignore', "target/")
grgit.add(patterns: ['.gitignore'])
grgit.commit (message: "Ignore maven artifacts")

Branch release1_0 = grgit.checkout (branch: 'release/1.0', createBranch: true)
createPom (sampleRemoteDir, "1.0.0")
grgit.add (patterns: ['pom.xml'])
grgit.commit (message: "Released version 1.0.0")
Tag releaseTag1_0 = grgit.tag.add (name: '1.0.0')

createPom (sampleRemoteDir, "1.0.1-SNAPSHOT")
grgit.add (patterns: ['pom.xml'])
grgit.commit (message: "Prepared next bugfix version")

develop = grgit.checkout (branch: 'develop')
grgit.merge (head: "release/1.0", mode: MergeOp.Mode.CREATE_COMMIT)
// release/1.0 was successfully merged, now just overwrite the pom.xml
createPom (sampleRemoteDir, "1.1.0-SNAPSHOT")
grgit.add (patterns: ['pom.xml'])
grgit.commit (message: "Prepared next minor development version (1.1.0)")

Branch release1_1 = grgit.checkout (branch: 'release/1.1', createBranch: true)
createPom (sampleRemoteDir, "1.1.0")
grgit.add (patterns: ['pom.xml'])
grgit.commit (message: "Released version 1.1.0")
Tag releaseTag1_1 = grgit.tag.add (name: '1.1.0')

createPom (sampleRemoteDir, "1.1.1-SNAPSHOT")
grgit.add (patterns: ['pom.xml'])
grgit.commit (message: "Prepared next bugfix version")

// Branch release/1.1 will not be merged back to development but left open
develop = grgit.checkout (branch: 'develop')
createPom (sampleRemoteDir, "1.2.0-SNAPSHOT")
grgit.add (patterns: ['pom.xml'])
grgit.commit (message: "Prepared next minor development version (1.2.0)")

// Finally clone everything to get remote references!
File sampleCloneDir = new File (sampleDir, "clone")
// sampleCloneDir.mkdirs()
Grgit clone = Grgit.clone (dir: "${sampleDir.absolutePath}/clone",
        remote: "origin",
        uri: "file:///${sampleRemoteDir.absolutePath}/.git")

assert (sampleCloneDir.exists())
