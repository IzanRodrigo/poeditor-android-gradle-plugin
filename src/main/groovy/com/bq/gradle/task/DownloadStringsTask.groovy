package com.bq.gradle.task

import groovy.json.JsonSlurper
import org.gradle.api.tasks.TaskAction

/**
 * Task that downloads all strings files (every available lang) from PoEditor given a apiToken and projectId.
 *
 * Created by imartinez on 11/1/16.
 */
class DownloadStringsTask extends POEditorTask {
   @TaskAction
   def downloadStrings() {
      // Retrieve available languages from PoEditor
      def jsonParser = new JsonSlurper()
      def langs = ['curl', '-X', 'POST', '-d', "api_token=${config.apiToken}", '-d', 'action=list_languages', '-d', "id=${config.projectId}", POEDITOR_API_URL].execute()
      def lagsText = langs.text
      def langsJson = jsonParser.parseText(lagsText)

      // Check if the response was 200
      if (langsJson.response.code != "200") {
         throw new IllegalStateException(
               "An error occurred while trying to export from PoEditor API: \n\n" +
                     langsJson.toString()
         )
      }

      // Clear previous download dir
      def dir = new File(config.downloadPath)
      if (!dir.exists()) dir.mkdir()
      dir.eachFile { it.delete() }

      // Iterate over every available language
      def desiredLangs = config.desiredLangs
      for (lang in langsJson.list) {
         if (desiredLangs == null || lang.code in desiredLangs) {
            download(lang, config.apiToken, config.projectId, dir)
         } else {
            println "Skipping Language: $lang.name"
         }
      }
   }

   private static void download(it, apiToken, projectId, File downloadDir) {
      // Retrieve translation file URL for the given language
      println "Retrieving translation file URL for language code: $it"
      // TODO curl may not be installed in the host SO. Add a safe check and, if curl is not available, stop the process and print an error message
      def translationFileInfo = ['curl', '-X', 'POST', '-d', "api_token=$apiToken", '-d', 'action=export', '-d', "id=$projectId", '-d', 'type=android_strings', '-d', "language=$it.code", POEDITOR_API_URL].execute()
      def translationFileInfoJson = new JsonSlurper().parseText(translationFileInfo.text)
      def translationFileUrl = translationFileInfoJson.item

      // Download translation File in "Android Strings" XML format
      println "Downloading file from Url: $translationFileUrl"
      def translationFile = new URL(translationFileUrl)
      def translationFileText = translationFile.getText('UTF-8')
            .replaceAll("<!--[\\s\\S]*?-->", "") // Remove comments.

      // Write downloaded and post-processed XML to files
      def fileName = "${createValuesModifierFromLangCode(it.code)}.xml"
      println "Writing $fileName file"
      new File(downloadDir, fileName).withWriter('UTF-8') { w ->
         w << translationFileText
      }
   }

   /**
    * Creates values file modifier taking into account specializations (i.e values-es-rMX for Mexican)
    * @param langCode
    * @return proper values file modifier (i.e. es-rMX)
    */
   private static def createValuesModifierFromLangCode(String langCode) {
      if (!langCode.contains("-")) {
         return langCode
      } else {
         String[] langParts = langCode.split("-")
         return langParts[0] + "-" + "r" + langParts[1].toUpperCase()
      }
   }
}
