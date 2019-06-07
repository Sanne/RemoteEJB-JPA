package org.jboss.prototypes.tools;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

public abstract class RuleGenerator implements RulesetGenerator {

    protected static void generateTemplate(String templateName, Map<String, String> params, PrintWriter out) {

        try {
            StringWriter writer = new StringWriter();
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache m = mf.compile(templateName);
            m.execute(writer, params).flush();
            out.println(writer.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
