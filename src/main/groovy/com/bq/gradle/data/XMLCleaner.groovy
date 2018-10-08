package com.bq.gradle.data

import com.vdurmont.emoji.EmojiParser

class XMLCleaner {
   static Map<String, String> clean(Map<String, String> entries, boolean useEmojis) {
      return entries.collectEntries {
         def newName = cleanName(it.key)
         def newValue = cleanValue(it.value, useEmojis)
         [(newName): newValue]
      } as Map<String, String>
   }

   // region - Internal

   private static def cleanName(String source) {
      return source.replaceAll("\\+", "_")
            .toLowerCase()
            .trim()
   }

   private static def cleanValue(String source, boolean useEmojis) {
      def str = useEmojis ? source : EmojiParser.removeAllEmojis(source)
      return str
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
