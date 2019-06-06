package org.jboss.as.quickstarts.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Clock implements Serializable  {

    @Id @GeneratedValue
    long id;

    String name;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Clock{" +
              "id=" + id +
              ", name='" + name + '\'' +
              '}';
    }
}
