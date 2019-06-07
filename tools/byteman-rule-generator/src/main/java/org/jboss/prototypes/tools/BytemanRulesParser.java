package org.jboss.prototypes.tools;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class BytemanRulesParser {

    static String METHODS_RESOURCE = "methods";
    static String LOG_FILE = "/tmp/timestamp.log";
    static String BYTEMAN_FILE = "/tmp/bytemanRules.btm";

    public static void main(String[] args) throws IOException {

        try (PrintWriter out = new PrintWriter(BYTEMAN_FILE)) {

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(BytemanRulesParser.class.getClassLoader().getResourceAsStream(METHODS_RESOURCE)))) {

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

                        generateTemplate(templateParams, out);

                        templateParams.put("PHASE", "EXIT");

                        generateTemplate(templateParams, out);

                    }

                } else {
                    throw new RuntimeException("Resource not found: " + METHODS_RESOURCE);
                }
            }
        }

    }

    private static void generateTemplate(Map<String, String> params, PrintWriter out) {

        try {
            StringWriter writer = new StringWriter();
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache m = mf.compile("bytemanRule.template");
            m.execute(writer, params).flush();
            out.println(writer.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
