package org.jboss.as.quickstarts.ejb.remote.client;

import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.as.quickstarts.ejb.remote.stateless.ClockLister;
import org.jboss.as.quickstarts.model.Clock;


public final class RemoteClient {

    private static final String HTTP = "http";

    public static void main(String[] args) throws Exception {
        ClockLister clockLister = lookupRemoteClocks();
        clockLister.storeNewClock(createNewClock());
        final List<Clock> clocks = clockLister.listAllClocks();
        for (Clock c : clocks) {
            System.out.println("Clock: " + c.getName());
        }
    }

    private static Clock createNewClock() {
        final Clock clock = new Clock();
        final String uuid = UUID.randomUUID().toString();
        System.out.println("Creating new clock " + uuid);
        clock.setName(uuid);
        return clock;
    }

    private static ClockLister lookupRemoteClocks() throws NamingException {

        final Hashtable<String, String> jndiProperties = new Hashtable<>();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        if(Boolean.getBoolean(HTTP)) {
            //use HTTP based invocation. Each invocation will be a HTTP request
            jndiProperties.put(Context.PROVIDER_URL,"http://localhost:8080/wildfly-services");
        } else {
            //use HTTP upgrade, an initial upgrade requests is sent to upgrade to the remoting protocol
            jndiProperties.put(Context.PROVIDER_URL,"remote+http://localhost:8080");
        }
        final Context context = new InitialContext(jndiProperties);

        // The JNDI lookup name for a stateless session bean has the syntax of:
        // ejb:<appName>/<moduleName>/<distinctName>/<beanName>!<viewClassName>
        //
        // <appName> The application name is the name of the EAR that the EJB is deployed in
        // (without the .ear). If the EJB JAR is not deployed in an EAR then this is
        // blank. The app name can also be specified in the EAR's application.xml
        //
        // <moduleName> By the default the module name is the name of the EJB JAR file (without the
        // .jar suffix). The module name might be overridden in the ejb-jar.xml
        //
        // <distinctName> : EAP allows each deployment to have an (optional) distinct name.
        // This example does not use this so leave it blank.
        //
        // <beanName> : The name of the session been to be invoked.
        //
        // <viewClassName>: The fully qualified classname of the remote interface. Must include
        // the whole package name.

        // let's do the lookup
        return (ClockLister) context.lookup("ejb:/ejb-remote-server-side/ClockListerBean!"
              + ClockLister.class.getName());
    }

}
