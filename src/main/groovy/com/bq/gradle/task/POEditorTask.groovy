package com.bq.gradle.task

import com.bq.gradle.data.POEditorConfig
import org.gradle.api.DefaultTask
import org.gradle.api.Project

class POEditorTask extends DefaultTask {
   protected static def POEDITOR_API_URL = "https://poeditor.com/api/"

   private POEditorConfig config

   protected POEditorConfig getConfig() {
      if (config == null) {
         config = initConfig(project)
      }

      return config
   }

   private static def initConfig(Project project) {
      final POEditorConfig config = project.extensions.POEditor

      Objects.requireNonNull(config, "The plugin must be configured using the POEditorConfig extension.")
      Objects.requireNonNull(config.apiToken, "apiToken == null")
      Objects.requireNonNull(config.projectId, "projectId == null")
      Objects.requireNonNull(config.defaultLang, "defaultLang == null")
      Objects.requireNonNull(config.downloadPath, "downloadPath == null")
      Objects.requireNonNull(config.resPath, "resPath == null")
      Objects.requireNonNull(config.fileName, "fileName == null")

      return config
   }
}
