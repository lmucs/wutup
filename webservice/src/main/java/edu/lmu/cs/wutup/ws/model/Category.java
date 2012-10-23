package edu.lmu.cs.wutup.ws.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "category")
public class Category {

    private Integer id;
    private String name;
    private Integer parentId;

    public Category() {
        this.id = null;
        this.name = null;
        this.parentId = null;
    }
    
    public Category(String name) {
        this.id = null;
        this.name = name;
        this.parentId = null;
    }
    
    public Category(String name, Integer parentId) {
        this.id = null;
        this.name = name;
        this.parentId = parentId;
    }
    
    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.parentId = null;
    }
    
    public Category(Integer id, String name, Integer parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    @XmlElement(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @XmlElement(name = "parentId")
    public Integer getParentId() {
        return this.parentId;
    }
    
    public void setParentId(int id) {
        this.parentId = id;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
