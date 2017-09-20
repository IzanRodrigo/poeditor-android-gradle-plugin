package com.bq.gradle

import javax.annotation.Nullable

/**
 * Extension class that represents the needed params that will
 * be passed to the different tasks of the plugin.
 *
 * Created by imartinez on 11/1/16.
 */
class PoEditorPluginExtension {
   /**
    * PoEditor API TOKEN
    */
   String apiToken = null

   /**
    * PoEditor PROJECT ID
    */
   String projectId = null

   /**
    * Default (and fallback) language code: i.e. "es", "en-rUS"
    */
   String defaultLang = null

   /**
    *  Path to res/ directory: i.e. "${project.rootDir}/app/src/main/res"
    */
   String destPath = null

   /**
    * File name for translations: i.e. "strings.xml"
    */
   String destFile = null

   /**
    * Discard languages if the translation progress is lower than this parameter.
    */
   @Nullable
   Float minLanguageProgress = null
}
