#!/usr/bin/env groovy

@Grab ('org.ajoberstar:grgit')
@Grab ('org.slf4j:slf4j-api')
@Grab ('org.slf4j:slf4j-simple')

import org.ajoberstar.grgit.Grgit
import org.eclipse.jgit.lib.Ref
import org.slf4j.Logger
import org.slf4j.LoggerFactory

Logger logger = LoggerFactory.getLogger("listTags")

Grgit grgit = Grgit.open (dir: ".git")

logger.info ("Opened git repository '{}'", grgit.repository)

List<Ref> tags = grgit.tag.list()

logger.info ("Found #{} tags", tags.size())

tags.each {tag->
    logger.info("Found tag '{}'", tag)
}

