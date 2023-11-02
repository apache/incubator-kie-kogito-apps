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
package org.kie.kogito.app.audit.jpa.queries;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.kie.kogito.app.audit.graphql.type.UserTaskInstanceAssignmentTO;
import org.kie.kogito.app.audit.graphql.type.UserTaskInstanceAttachmentTO;
import org.kie.kogito.app.audit.graphql.type.UserTaskInstanceCommentTO;
import org.kie.kogito.app.audit.graphql.type.UserTaskInstanceDeadlineTO;
import org.kie.kogito.app.audit.graphql.type.UserTaskInstanceStateTO;
import org.kie.kogito.app.audit.graphql.type.UserTaskInstanceVariableTO;
import org.kie.kogito.app.audit.spi.GraphQLSchemaQuery;
import org.kie.kogito.app.audit.spi.GraphQLSchemaQueryProvider;

import graphql.com.google.common.base.Objects;

public class JPAGraphQLSchemaUserTaskInstancesQueryProvider implements GraphQLSchemaQueryProvider {

    @Override
    public List<GraphQLSchemaQuery<?>> queries() {
        return List.of(
                new JPASimpleNamedQuery<UserTaskInstanceStateTO>("GetAllUserTaskInstanceState", "GetAllUserTaskInstanceState", UserTaskInstanceStateTO.class),
                new JPASimpleNamedQuery<UserTaskInstanceAttachmentTO>("GetAllUserTaskInstanceAttachments", "GetAllUserTaskInstanceAttachments", UserTaskInstanceAttachmentTO.class),
                new JPASimpleNamedQuery<UserTaskInstanceCommentTO>("GetAllUserTaskInstanceComment", "GetAllUserTaskInstanceComment", UserTaskInstanceCommentTO.class),
                new JPASimpleNamedQuery<UserTaskInstanceDeadlineTO>("GetAllUserTaskInstanceDeadline", "GetAllUserTaskInstanceDeadline", UserTaskInstanceDeadlineTO.class),
                new JPASimpleNamedQuery<UserTaskInstanceVariableTO>("GetAllUserTaskInstanceVariable", "GetAllUserTaskInstanceVariable", UserTaskInstanceVariableTO.class),
                new JPAComplexNamedQuery<UserTaskInstanceAssignmentTO>("GetAllUserTaskInstanceAssignments", "GetAllUserTaskInstanceAssignments", new DataMapper<UserTaskInstanceAssignmentTO>() {

                    @Override
                    public List<UserTaskInstanceAssignmentTO> produce(List<Object[]> data) {
                        List<UserTaskInstanceAssignmentTO> transformedData = new ArrayList<>();
                        UserTaskInstanceAssignmentTO current = null;
                        Object currentIndex = null;
                        for (int idx = 0; idx < data.size(); idx++) {
                            Object[] row = data.get(idx);
                            if (!Objects.equal(currentIndex, row[0])) {
                                current = new UserTaskInstanceAssignmentTO();
                                currentIndex = row[0];
                                transformedData.add(current);
                            }
                            current.setEventId((String) row[0]);
                            current.setEventDate(toDateTime((Date) row[1]));
                            current.setEventUser((String) row[2]);
                            current.setUserTaskDefinitionId((String) row[3]);
                            current.setUserTaskInstanceId((String) row[4]);
                            current.setProcessInstanceId((String) row[5]);
                            current.setBusinessKey((String) row[6]);
                            current.setUserTaskName((String) row[7]);
                            current.setAssignmentType((String) row[8]);
                            current.addUser((String) data.get(idx)[9]);

                        }

                        return transformedData;
                    }

                }));

    }

    public OffsetDateTime toDateTime(Date date) {
        return (date != null) ? OffsetDateTime.ofInstant(date.toInstant(), ZoneId.of("UTC")) : null;
    }

}
