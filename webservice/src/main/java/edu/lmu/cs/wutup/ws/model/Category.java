package edu.lmu.cs.wutup.ws.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

@XmlRootElement(name = "category")
public class Category {

    private Integer id;
    private String name;
    private Integer parentId;

    public Category() {
        this(null, null, null);
    }
    
    public Category(String name) {
        this(null, name, null);
    }
    
    public Category(String name, Integer parentId) {
        this(null, name, parentId);
    }
    
    public Category(Integer id, String name) {
        this(id, name, null);
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
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.id, this.name, this.parentId);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Category
                && Objects.equal(this.id, Category.class.cast(obj).getId())
                && Objects.equal(this.parentId, Category.class.cast(obj).getParentId())
                && Objects.equal(this.name, Category.class.cast(obj).getName());
    }
}
