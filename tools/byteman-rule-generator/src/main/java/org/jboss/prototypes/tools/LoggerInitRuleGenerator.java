package org.jboss.prototypes.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class LoggerInitRuleGenerator extends RuleGenerator {

    private static final String TEMPLATE_NAME = "bytemanLoggerInitRule.template";

    public LoggerInitRuleGenerator() {
    }

    public void generateRules(BufferedReader reader, PrintWriter out) throws IOException {

        Set<String> loggerSet = new TreeSet<>();

        if (reader != null) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] instrumentationPoint = line.split(":");

                if (!loggerSet.contains(instrumentationPoint[2])) {
                    Map<String, String> templateParams = new HashMap<>();

                    templateParams.put("PACKAGE", (instrumentationPoint[4].equals("Y") ? "^" : "") + instrumentationPoint[1]);
                    templateParams.put("CLASS", instrumentationPoint[2]);
                    templateParams.put("TYPE", instrumentationPoint[3]);

                    generateTemplate(TEMPLATE_NAME, templateParams, out);
                    loggerSet.add(instrumentationPoint[2]);
                }

            }
        }


    }
}
