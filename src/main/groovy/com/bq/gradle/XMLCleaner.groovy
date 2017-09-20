package com.bq.gradle

public class XMLCleaner {
   public static String clean(String source) {
      def entries = new XmlSlurper()
            .parseText(source.trim())
            .string
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

   private static String cleanEntry(String name, String value) {
      def cleanName = cleanName(name)
      def cleanValue = cleanValue(value)
      return '<string name="' + cleanName + '">' + cleanValue + '</string>'
   }

   private static String cleanName(String source) {
      return source.replaceAll("\\+", "_")
            .toLowerCase()
   }

   private static String cleanValue(String source) {
      return source.replaceAll(" *\\...", "…")
            .replaceAll("%%@", "%s")
            .replaceAll("%@", "%s")
   }
}