package com.bq.gradle.data

class XMLCleaner {
   static Map<String, String> clean(Map<String, String> entries) {
      return entries.collectEntries {
         def newName = cleanName(it.key)
         def newValue = cleanValue(it.value)
         [(newName): newValue]
      } as Map<String, String>
   }

   // region - Internal

   private static def cleanName(String source) {
      return source.replaceAll("\\+", "_")
            .toLowerCase()
            .trim()
   }

   private static def cleanValue(String source) {
      return source
            .replaceAll(" *\\.\\.\\.", "…") // Use recommended ellipsis character '…'.
            .replaceAll("&", "&amp;") // Encode '&' character.
            .replaceAll("%@s", "%s") // \
            .replaceAll("%%@", "%s") //  > Format iOS strings.
            .replaceAll("%@", "%s")  // /
            .replaceAll("\\n\\s*\"", "\"") // Trim strings.
            .trim()
   }

   // endregion
}
