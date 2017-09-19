package com.bq.gradle

public class XMLCleaner {
   public static String clean(String source) {
      def matcher = source =~ '<string name="(.*)">(.*)</string>'

      def entries = new ArrayList<String>()
      while (matcher.find()) {
         def name = matcher.group(1)
         def value = matcher.group(2)
         def entry = cleanEntry(name, value)
         entries.add(entry)
      }
      Collections.sort(entries)

      def cleanXml = new StringBuilder("<resources>")
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
      return source.replaceAll("\\...", "…")
            .replaceAll('"(?i)Ok"', "@android:string/ok")
            .replaceAll('"(?i)Cancel"', "@android:string/cancel")
            .replaceAll("%%@", "%s")
            .replaceAll("%@", "%s")
   }
}