package com.bq.gradle

class XMLCleaner {
   static String clean(String source) {
      def entries = new XmlSlurper()
            .parseText(source.trim())
            .string
            .findAll { it.text().length() > 0 }
            .collect {
         def name = it.@name.text()
         def value = it.text()
         cleanEntry(name, value)
      }
      .sort()

      def cleanXml = new StringBuilder('<?xml version="1.0" encoding="utf-8"?>')
      cleanXml.append("\n<resources>")
      for (entry in entries) {
         cleanXml.append("\n\t" + entry)
      }
      cleanXml.append("\n</resources>")

      return cleanXml.toString()
   }

   private static def cleanEntry(String name, String value) {
      def cleanName = cleanName(name)
      def cleanValue = cleanValue(value)
      return '<string name="' + cleanName + '">' + cleanValue + '</string>'
   }

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
}
