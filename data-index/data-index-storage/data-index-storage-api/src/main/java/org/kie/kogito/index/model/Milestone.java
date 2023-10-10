package org.kie.kogito.index.model;

public class Milestone {

    private String id;
    private String name;
    private String status;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Milestone)) {
            return false;
        }

        Milestone that = (Milestone) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return "Milestone{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Milestone milestone;

        private Builder() {
            milestone = new Milestone();
        }

        public Builder id(String id) {
            milestone.setId(id);
            return this;
        }

        public Builder name(String name) {
            milestone.setName(name);
            return this;
        }

        public Builder status(String status) {
            milestone.setStatus(status);
            return this;
        }

        public Milestone build() {
            return milestone;
        }
    }
}
