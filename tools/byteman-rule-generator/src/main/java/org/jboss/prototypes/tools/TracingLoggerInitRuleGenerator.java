package org.jboss.prototypes.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class TracingLoggerInitRuleGenerator extends RuleGenerator {

    private static final String TEMPLATE_NAME = "bytemanLoggerRule.template";

    public TracingLoggerInitRuleGenerator() {
    }

    public void generateRules(BufferedReader reader, PrintWriter out) throws IOException {

        Set<String> loggerSet = new TreeSet<>();

        if (reader != null) {
            throw new RuntimeException("This rule generator does not expect input file");
        }
        Map<String, String> templateParams = new HashMap<>();


        templateParams.put("PACKAGE", "org.jboss.as.server");
        templateParams.put("CLASS", "ApplicationServerService");
        templateParams.put("METHOD", "start");
        templateParams.put("TYPE", "CLASS");

        generateTemplate(TEMPLATE_NAME, templateParams, out);

    }

}
