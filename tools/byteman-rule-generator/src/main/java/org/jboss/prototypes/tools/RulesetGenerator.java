package org.jboss.prototypes.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public interface RulesetGenerator {

    void generateRules(BufferedReader reader, PrintWriter out) throws IOException;
}
