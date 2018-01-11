package com.bq.gradle.data

import javax.annotation.Nullable

/**
 * Extension class that represents the needed params that will
 * be passed to the different tasks of the plugin.
 *
 * Created by imartinez on 11/1/16.
 */
class POEditorConfig {
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
    *  Specify the download directory.
    */
   String downloadPath = null

   /**
    *  Path to res/ directory: i.e. "${project.rootDir}/app/src/main/res"
    */
   String resPath = null

   /**
    * File name for translations: i.e. "strings.xml"
    */
   String fileName = null

   /**
    * Download only the specified languages (if any).
    */
   @Nullable
   List<String> desiredLangs = null

   @Override
   String toString() {
      return "POEditorConfig{" +
            "apiToken='" + apiToken + '\'' +
            ", projectId='" + projectId + '\'' +
            ", defaultLang='" + defaultLang + '\'' +
            ", downloadPath='" + downloadPath + '\'' +
            ", resPath='" + resPath + '\'' +
            ", fileName='" + fileName + '\'' +
            ", minLanguageProgress=" + minLanguageProgress +
            '}';
   }
}
