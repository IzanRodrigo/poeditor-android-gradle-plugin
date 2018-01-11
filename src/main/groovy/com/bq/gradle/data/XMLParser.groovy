package com.bq.gradle.data

class XMLParser {
   static Map<String, String> parse(String source) {
      return new XmlSlurper()
            .parseText(source.trim())
            .string
            .findAll { it.text().length() > 0 }
            .collectEntries {
         def name = it.@name.text()
         def value = it.text()
         [(name): value]
      }
      .sort()
   }

   static def buildXml(Map<String, String> strings) {
      def cleanXml = new StringBuilder('<?xml version="1.0" encoding="utf-8"?>')
      cleanXml.append("\n<resources>")
      for (string in strings) {
         def entry = '<string name="' + string.key + '">' + string.value + '</string>'
         cleanXml.append("\n\t" + entry)
      }
      cleanXml.append("\n</resources>")
      return cleanXml.toString()
   }
}
