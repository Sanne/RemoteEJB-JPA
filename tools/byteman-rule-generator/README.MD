EAP Subsystem tracing Rule Generator
====================================
This project generates byteman rules for tracing EAP system timings, with logging to a custom Jboss Logging category.

Building Rules
--------------

To build custom tracing rules for EAP;

`mvn clean compile exec:exec`

Tracing rules will be written to; 

`/tmp/bytemanRules.btm`

 Enabling Rules in EAP
 ---------------------
 
 1. Download byteman - https://downloads.jboss.org/byteman/4.0.7/byteman-download-4.0.7-bin.zip 
 
 2. Unzip byteman
 
 `unzip byteman-download-4.0.7-bin.zip`
 
 3. Set BYTEMAN_HOME environment variable
 
 `export BYTEMAN_HOME=*PATH_TO_BYTEMAN_DIR*`
 
 4. Modify standalone.conf in JBOSS_HOME/bin

```` 
 if [ "x$JAVA_OPTS" = "x" ]; then
 ...
    JAVA_OPTS="$JAVA_OPTS -javaagent:${BYTEMAN_HOME}/lib/byteman.jar=boot:${BYTEMAN_HOME}/lib/byteman.jar,script:/tmp/bytemanRules.btm,sys:${BYTEMAN_HOME}/lib/byteman.jar -Dorg.jboss.byteman.transform.all"
 ...
 else
    echo "JAVA_OPTS already set in environment; overriding default settings with values: $JAVA_OPTS"
 fi
 ````
 
 Configuring Asynchronous Logging
 --------------------------------
 
 1. Add the following configuration to `JBOSS_HOME/standalone/config/standalone-full.xml`
 
 ````
 <subsystem xmlns="urn:jboss:domain:logging:6.0">
              <async-handler name="AsyncSubsystemTracing">
                 <level name="INFO"/>
                 <queue-length value="1000"/>
                 <overflow-action value="block"/>
                 <subhandlers>
                     <handler name="SubsystemTracing"/>
                 </subhandlers>
             </async-handler>
             <file-handler name="SubsystemTracing">
                 <formatter>
                     <named-formatter name="PLAIN"/>
                 </formatter>
                 <file relative-to="user.home" path="subsystem-tracing.out"/>
             </file-handler>
             <logger category="org.jboss.tracing.SubsystemTracing" use-parent-handlers="false">
                 <level name="INFO"/>
                 <handlers>
                     <handler name="AsyncSubsystemTracing"/>
                 </handlers>
             </logger>
             <formatter name="PLAIN">
                 <pattern-formatter pattern="%s%e%n"/>
             </formatter>
             
...            
 ````
 
 Subsystem tracing will now be written to : `~/subsystem-tracing.out`