package org.jboss.prototypes.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ThreadStringBuilderRuleGenerator extends RuleGenerator {

    private static final String TEMPLATE_NAME = "bytemanStringBuilderRule.template";

    public ThreadStringBuilderRuleGenerator() {
    }

    public void generateRules(BufferedReader reader, PrintWriter out) throws IOException {

        if (reader != null) {
            throw new RuntimeException("This rule generator does not expect input file");
        }

        generateTemplate(TEMPLATE_NAME, new HashMap<>(), out);

    }

}
