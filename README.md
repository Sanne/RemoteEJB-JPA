# RemoteEJB-JPA
Experimenting with JPA access over Remote EJBs

Build & run:

    mvn clean package wildfly:deploy
    cd client
    mvn exec:exec

Each time the client is run it will store some more data in the Database.
The Database is backed by H2: it's reset on re-deploy.

