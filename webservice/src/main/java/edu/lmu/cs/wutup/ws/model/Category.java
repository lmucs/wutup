package edu.lmu.cs.wutup.ws.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "category")
public class Category {

    private Integer id;
    private Integer parentId;
    private String name;

    public Category() {
        this.parentId = null;
    }
    
    public Category(int parentId) {
        this.parentId = parentId;
    }

    public Category(String name, Integer id) {
        this.id = id;
        this.name = name;
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
