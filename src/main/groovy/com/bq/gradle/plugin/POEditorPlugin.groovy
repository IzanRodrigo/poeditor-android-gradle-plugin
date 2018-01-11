package com.bq.gradle.plugin

import com.bq.gradle.data.POEditorConfig
import com.bq.gradle.task.DownloadStringsTask
import com.bq.gradle.task.ProcessStringsTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * PoEditor gradle plugin.
 *
 * Created by imartinez on 11/1/16.
 */
class POEditorPlugin implements Plugin<Project> {
   void apply(Project project) {
      // Add the 'POEditorConfig' extension object, used to pass parameters to the task
      project.extensions.create("POEditor", POEditorConfig)

      // Register the tasks.
      project.task('downloadStrings', type: DownloadStringsTask)
      project.task("processStrings", type: ProcessStringsTask)
   }
}