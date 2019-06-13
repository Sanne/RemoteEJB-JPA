package org.jboss.prototypes.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class TraceRuleGenerator extends RuleGenerator {

    static String LOG_FILE = "/tmp/timestamp.log";

    public TraceRuleGenerator() {
    }

    public void generateRules(BufferedReader reader, PrintWriter out) throws IOException {

        if (reader != null) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] instrumentationPoint = line.split(":");
                Map<String, String> templateParams = new HashMap<>();

                templateParams.put("SUBSYSTEM", instrumentationPoint[0]);
                templateParams.put("PACKAGE", (instrumentationPoint[4].equals("Y") ? "^" : "") + instrumentationPoint[1]);
                templateParams.put("CLASS", instrumentationPoint[2]);
                templateParams.put("TYPE", instrumentationPoint[3]);
                templateParams.put("METHOD", instrumentationPoint[5]);
                templateParams.put("PHASE", "ENTRY");
                templateParams.put("LOG_FILE", LOG_FILE);

                generateTemplate("bytemanTraceRule.template", templateParams, out);

                templateParams.put("PHASE", "EXIT");

                generateTemplate("bytemanTraceRule.template", templateParams, out);

            }
        } else {
            throw new RuntimeException("This rule generator expects input file!");
        }

    }
}
