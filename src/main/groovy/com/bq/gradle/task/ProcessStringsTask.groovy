package com.bq.gradle.task

import com.bq.gradle.data.XMLCleaner
import com.bq.gradle.data.XMLParser
import groovy.io.FileType
import org.gradle.api.tasks.TaskAction

class ProcessStringsTask extends POEditorTask {
   @TaskAction
   def processStrings() {
      def dir = new File(config.downloadPath)
      if (!dir.exists()) dir.mkdir()
      dir.eachFile(FileType.FILES) {
         if (it.name.contains(".")) {
            def lang = it.name.take(it.name.lastIndexOf("."))
            def sourceStrings = XMLParser.parse(it.getText("UTF-8"))

            // Write strings without emojis.
            def cleanStrings = XMLCleaner.clean(sourceStrings, false)
            def xml = XMLParser.buildXml(cleanStrings)
            def destDirName = (lang == config.defaultLang) ? "values" : "values-$lang"
            def destDir = new File(config.resPath, destDirName)
            destDir.mkdir()
            def destFileName = config.fileName ?: "strings.xml"
            def destFile = new File(destDir, destFileName)
            destFile.write(xml, "UTF-8")

            // Write strings with emojis for Android M+
            cleanStrings = XMLCleaner.clean(sourceStrings, true)
            xml = XMLParser.buildXml(cleanStrings)
            destDirName = (lang == config.defaultLang) ? "values-v23" : "values-$lang-v23"
            destDir = new File(config.resPath, destDirName)
            destDir.mkdir()
            destFileName = config.fileName ?: "strings.xml"
            destFile = new File(destDir, destFileName)
            destFile.write(xml, "UTF-8")
         }
      }
   }
}
