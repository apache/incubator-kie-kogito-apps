/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.index.jdbc;

import java.util.Map;
import java.util.Objects;

import org.kie.kogito.index.jpa.model.ProcessDefinitionEntity;
import org.kie.kogito.index.model.ProcessDefinitionKey;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;

@Entity(name = "definitions")
@Table(name = "definitions")
@IdClass(ProcessDefinitionKey.class)
public class JPAProcessDefinitionEntity extends ProcessDefinitionEntity {

    @ElementCollection
    @CollectionTable(name = "definitions_metadata", joinColumns = {
            @JoinColumn(name = "process_id", referencedColumnName = "id"), @JoinColumn(name = "process_version", referencedColumnName = "version") },
            foreignKey = @ForeignKey(name = "fk_definitions_metadata"))
    @MapKeyColumn(name = "name")
    @Column(name = "meta_value")
    private Map<String, String> metadata;

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(metadata);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        JPAProcessDefinitionEntity other = (JPAProcessDefinitionEntity) obj;
        return Objects.equals(metadata, other.metadata);
    }

}
