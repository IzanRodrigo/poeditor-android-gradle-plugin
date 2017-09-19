package com.bq.gradle

/**
 * Extension class that represents the needed params that will
 * be passed to the different tasks of the plugin.
 *
 * Created by imartinez on 11/1/16.
 */
class PoEditorPluginExtension {
   // PoEditor API TOKEN
   String apiToken = null

   // PoEditor PROJECT ID
   String projectId = null

   // Default (and fallback) language code: i.e. "es"
   String defaultLang = null

   // Path to res/ directory: i.e. "${project.rootDir}/app/src/main/res"
   String destPath = null

   // File name for translations: i.e. "strings.xml"
   String destFile = "strings.xml"

   // Download languages
   float minLanguageProgress = 95.0
}
