package edu.lmu.cs.wutup.ws.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

@XmlRootElement(name = "category")
public class Category {

        private int id;
        private String name;

        public Category() {
            // No-arg constructor required for annotations
           }

           public Category(String name) {
               this.name = name;
            }

}