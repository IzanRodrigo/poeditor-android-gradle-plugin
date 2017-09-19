package com.bq.gradle

import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Task that:
 * 1. downloads all strings files (every available lang) from PoEditor given a api_token and project_id.
 * 2. extract "tablet" strings to another own XML (strings with the suffix "_tablet")
 * 3. creates and saves two strings.xml files to values-<lang> and values-<lang>-sw600dp (tablet specific strings)
 *
 * Created by imartinez on 11/1/16.
 */
class ImportPoEditorStringsTask extends DefaultTask {
   private static def POEDITOR_API_URL = 'https://poeditor.com/api/'

   @TaskAction
   def importPoEditorStrings() {
      // Read config
      PoEditorPluginExtension args = project.extensions.poEditorPlugin
      def apiToken = Objects.requireNonNull(args.apiToken, "apiToken == null")
      def projectId = Objects.requireNonNull(args.projectId, "projectId == null")
      def defaultLang = Objects.requireNonNull(args.defaultLang, "defaultLang == null")
      def resDirPath = Objects.requireNonNull(args.destPath, "destPath == null")
      def fileName = Objects.requireNonNull(args.destFile, "destFile == null")
      def minLanguageProgress = args.minLanguageProgress;

      // Retrieve available languages from PoEditor
      def jsonParser = new JsonSlurper()
      def langs = ['curl', '-X', 'POST', '-d', "api_token=${apiToken}", '-d', 'action=list_languages', '-d', "id=${projectId}", POEDITOR_API_URL].execute()
      def langsJson = jsonParser.parseText(langs.text)

      // Check if the response was 200
      if (langsJson.response.code != "200") {
         throw new IllegalStateException(
               "An error occurred while trying to export from PoEditor API: \n\n" +
                     langsJson.toString()
         )
      }

      // Iterate over every available language
      for (lang in langsJson.list) {
         if (lang.percentage > minLanguageProgress) {
            parseLanguage(lang, apiToken, projectId, resDirPath, defaultLang, fileName)
         } else {
            println("Skipping Langague: ${lang.name}")
         }
      }
   }

   private static void parseLanguage(it, apiToken, projectId, resDirPath, defaultLang, fileName) {
      // Retrieve translation file URL for the given language
      println "Retrieving translation file URL for language code: ${it}"
      // TODO curl may not be installed in the host SO. Add a safe check and, if curl is not available, stop the process and print an error message
      def translationFileInfo = ['curl', '-X', 'POST', '-d', "api_token=${apiToken}", '-d', 'action=export', '-d', "id=${projectId}", '-d', 'type=android_strings', '-d', "language=${it.code}", POEDITOR_API_URL].execute()
      def translationFileInfoJson = new JsonSlurper().parseText(translationFileInfo.text)
      def translationFileUrl = translationFileInfoJson.item

      // Download translation File in "Android Strings" XML format
      println "Downloading file from Url: ${translationFileUrl}"
      //def translationFile = ['curl', '-H', '"charset=UTF-8"', '-X', 'GET', translationFileUrl].execute()
      def translationFile = new URL(translationFileUrl)

      // Post process the downloaded XML:
      def translationFileText = XMLCleaner.clean(translationFile.getText('UTF-8'))

      // If language folders doesn't exist, create it.
      // TODO investigate if we can infer the res folder path instead of passing it using poEditorPlugin.res_dir_path
      def valuesModifier = createValuesModifierFromLangCode(it.code)
      def valuesFolder = valuesModifier != defaultLang ? "values-${valuesModifier}" : "values"

      def stringsFolder = new File("${resDirPath}/${valuesFolder}")
      if (!stringsFolder.exists()) {
         println 'Creating strings folder for new language'
         def folderCreated = stringsFolder.mkdir()
         println "Folder created: ${folderCreated}"
      }

      // Write downloaded and post-processed XML to files
      println "Writing $fileName file"
      new File(stringsFolder, fileName).withWriter('UTF-8') { w ->
         w << translationFileText
      }
   }

   /**
    * Creates values file modifier taking into account specializations (i.e values-es-rMX for Mexican)
    * @param langCode
    * @return proper values file modifier (i.e. es-rMX)
    */
   private static String createValuesModifierFromLangCode(String langCode) {
      if (!langCode.contains("-")) {
         return langCode
      } else {
         String[] langParts = langCode.split("-")
         return langParts[0] + "-" + "r" + langParts[1].toUpperCase()
      }
   }
}
