package org.jboss.prototypes.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class BytemanRulesGenerator {

    static String BYTEMAN_FILE = "/tmp/bytemanRules.btm";
    static String METHODS_RESOURCE = "traceMethods";

    public static void main(String[] args) throws FileNotFoundException {

        try (PrintWriter out = new PrintWriter(BYTEMAN_FILE)) {

            generateRuleSetNoInput(out, new TracingLoggerInitRuleGenerator());
            generateRuleSetNoInput(out, new ThreadStringBuilderRuleGenerator());
            generateRuleSet(out, new TraceRuleGenerator());
        }
    }

    private static void generateRuleSet(PrintWriter out, RulesetGenerator generator) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(BytemanRulesGenerator.class.getClassLoader().getResourceAsStream(METHODS_RESOURCE)))) {

            generator.generateRules(reader, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static void generateRuleSetNoInput(PrintWriter out, RulesetGenerator generator) {
        try {
            generator.generateRules(null, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
