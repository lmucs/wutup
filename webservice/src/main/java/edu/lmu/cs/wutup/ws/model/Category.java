package edu.lmu.cs.wutup.ws.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Builder;

@XmlRootElement(name = "category")
@Data
@Builder
@EqualsAndHashCode
public class Category {

    @Getter(onMethod=@__({@XmlElement(name = "id")}))
    private Integer id;

    @Getter(onMethod=@__({@XmlElement(name = "name")}))
    private String name;

    @Getter(onMethod=@__({@XmlElement(name = "parentId")}))
    private Integer parentId;

}
