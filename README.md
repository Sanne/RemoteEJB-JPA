# RemoteEJB-JPA
Experimenting with JPA access over Remote EJBs

This is assuming that JBoss EAP7 is already started. No configuration changes are required on the server side.

Build & deploy the server to a running EAP instance:

    mvn clean package wildfly:deploy


Build & run the client:

    cd client
    mvn exec:exec


The Database is backed by H2: it's reset on re-deploy.

