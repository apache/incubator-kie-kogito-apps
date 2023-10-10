package org.kie.kogito.index.postgresql.model;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity(name = "milestones")
@IdClass(MilestoneEntityId.class)
@Table(name = "milestones")
public class MilestoneEntity extends AbstractEntity {

    @Id
    private String id;

    @Id
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "processInstanceId", foreignKey = @ForeignKey(name = "fk_milestones_process"))
    private ProcessInstanceEntity processInstance;
    private String name;
    private String status;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProcessInstanceEntity getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstanceEntity processInstance) {
        this.processInstance = processInstance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MilestoneEntity that = (MilestoneEntity) o;
        return id.equals(that.id) && processInstance.equals(that.processInstance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, processInstance);
    }
}
