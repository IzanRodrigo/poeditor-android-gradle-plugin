package com.bq.gradle.task

import com.bq.gradle.data.XMLCleaner
import com.bq.gradle.data.XMLParser
import groovy.io.FileType
import org.gradle.api.tasks.TaskAction

class ProcessStringsTask extends POEditorTask {
   @TaskAction
   def processStrings() {
      new File(config.downloadPath).eachFile(FileType.FILES) {
         if (it.name.contains(".")) {
            def lang = it.name.take(it.name.lastIndexOf("."))
            def sourceStrings = XMLParser.parse(it.getText("UTF-8"))
            def cleanStrings = XMLCleaner.clean(sourceStrings, config.useEmojis)
            def xml = XMLParser.buildXml(cleanStrings)
            def destDirName = (lang == config.defaultLang) ? "values" : "values-$lang"
            def destDir = new File(config.resPath, destDirName)
            destDir.mkdir()
            def destFileName = config.fileName ?: "strings.xml"
            def destFile = new File(destDir, destFileName)
            destFile.write(xml, "UTF-8")
         }
      }
   }
}
