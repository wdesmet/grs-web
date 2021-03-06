package net.straininfo2.grs.dto;

import net.straininfo2.grs.bioproject.mappings.Mapping;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="mapping")
public class MappingDto {

    @XmlElement
    String url;

    @XmlElement
    String subjectType;

    @XmlElement
    String linkName;

    @XmlElement
    String category;

    @XmlElement
    String sourceId;

    @XmlElement
    long providerId;

    @XmlElement
    long projectId;

    public MappingDto() {
    }

    public MappingDto(Mapping mapping) {
        this.url = mapping.getUrl();
        this.subjectType = mapping.getSubjectType();
        this.linkName = mapping.getLinkName();
        this.category = mapping.getCategory().getName();
        this.sourceId = mapping.getTargetId();
        this.providerId = mapping.getProvider().getId();
        this.projectId = mapping.getBioProject().getProjectId();
    }
}
