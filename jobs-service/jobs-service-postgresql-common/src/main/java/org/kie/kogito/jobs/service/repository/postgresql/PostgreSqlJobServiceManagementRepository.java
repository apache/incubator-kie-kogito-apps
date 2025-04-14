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
package org.kie.kogito.jobs.service.repository.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import javax.sql.DataSource;

import org.kie.kogito.jobs.service.model.JobServiceManagementInfo;
import org.kie.kogito.jobs.service.repository.JobServiceManagementRepository;
import org.kie.kogito.jobs.service.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PostgreSqlJobServiceManagementRepository implements JobServiceManagementRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostgreSqlJobServiceManagementRepository.class);

    private DataSource client;

    @Inject
    public PostgreSqlJobServiceManagementRepository(DataSource client) {
        this.client = client;
    }

    private static final String GET_AND_UPDATE = "SELECT id, token, last_heartbeat FROM job_service_management WHERE id = ? FOR UPDATE ";
    private static final String RELEASE = "UPDATE job_service_management SET token = null, last_heartbeat = null WHERE id = ? AND token = ? RETURNING id, token, last_heartbeat";
    private static final String HEARTBEAT = "UPDATE job_service_management SET last_heartbeat = now() WHERE id = ? AND token = ? RETURNING id, token, last_heartbeat";
    private static final String UPDATE = "INSERT INTO job_service_management (id, token, last_heartbeat) " +
            "VALUES (?, ?, ?) " +
            "ON CONFLICT (id) DO " +
            "UPDATE SET token = ?, last_heartbeat = ? " +
            "RETURNING id, token, last_heartbeat";

    public JobServiceManagementInfo getAndUpdate(String id, Function<JobServiceManagementInfo, JobServiceManagementInfo> computeUpdate) {
        LOGGER.trace("get {}", id);
        JobServiceManagementInfo jobServiceManagementInfo = null;
        try (Connection connection = client.getConnection(); PreparedStatement stmt = connection.prepareStatement(GET_AND_UPDATE)) {
            stmt.setString(1, id);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                jobServiceManagementInfo = from(resultSet);
                LOGGER.trace("got {}", jobServiceManagementInfo);
            }
            resultSet.close();
            return update(connection, computeUpdate.apply(jobServiceManagementInfo));
        } catch (SQLException ex) {
            LOGGER.error("Error during getAndUpdate job service management info", ex);
            return null;
        }

    }

    JobServiceManagementInfo from(ResultSet row) throws SQLException {
        OffsetDateTime lastHeartbeat = Optional.ofNullable(row.getTimestamp("last_heartbeat"))
                .map(Timestamp::getTime)
                .map(Date::new)
                .map(DateUtil::dateToOffsetDateTime)
                .orElse(null);

        return new JobServiceManagementInfo(
                row.getString("id"),
                row.getString("token"),
                lastHeartbeat);
    }

    @Override
    public JobServiceManagementInfo set(JobServiceManagementInfo info) {
        LOGGER.trace("set {}", info);
        try (Connection connection = client.getConnection();) {
            return update(connection, info);
        } catch (SQLException ex) {
            LOGGER.error("Error during set job service management info", ex);
            return null;
        }
    }

    private JobServiceManagementInfo update(Connection connection, JobServiceManagementInfo info) {
        if (Objects.isNull(info)) {
            return null;
        }
        java.sql.Timestamp heartbeat = Optional.ofNullable(info.getLastHeartbeat())
                .map(DateUtil::toDate)
                .map(Date::getTime)
                .map(java.sql.Timestamp::new)
                .orElse(null);

        JobServiceManagementInfo jobServiceManagementInfo = null;
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE)) {
            stmt.setString(1, info.getId());
            stmt.setString(2, info.getToken());
            stmt.setTimestamp(3, heartbeat);

            stmt.setString(4, info.getToken());
            stmt.setTimestamp(5, heartbeat);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                jobServiceManagementInfo = from(resultSet);
                LOGGER.trace("update {}", jobServiceManagementInfo);
            }
            resultSet.close();
            return jobServiceManagementInfo;
        } catch (SQLException ex) {
            LOGGER.error("Error during update job service management info", ex);
            return null;
        }

    }

    @Override
    public JobServiceManagementInfo heartbeat(JobServiceManagementInfo info) {
        JobServiceManagementInfo jobServiceManagementInfo = null;
        try (Connection connection = client.getConnection(); PreparedStatement stmt = connection.prepareStatement(HEARTBEAT)) {
            stmt.setString(1, info.getId());
            stmt.setString(2, info.getToken());

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                jobServiceManagementInfo = from(resultSet);
                LOGGER.trace("heartbeat {}", jobServiceManagementInfo);
            }
            resultSet.close();
            return jobServiceManagementInfo;
        } catch (SQLException ex) {
            LOGGER.error("Error during heartbeat job service management info", ex);
            return null;
        }
    }

    @Override
    public Boolean release(JobServiceManagementInfo info) {
        JobServiceManagementInfo updatedInfo = null;
        try (Connection connection = client.getConnection(); PreparedStatement stmt = connection.prepareStatement(RELEASE)) {
            stmt.setString(1, info.getId());
            stmt.setString(2, info.getToken());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                updatedInfo = from(resultSet);
            }
            resultSet.close();
            return updatedInfo != null;
        } catch (SQLException ex) {
            LOGGER.error("Error during release job service management info", ex);
            return null;
        }

    }
}
