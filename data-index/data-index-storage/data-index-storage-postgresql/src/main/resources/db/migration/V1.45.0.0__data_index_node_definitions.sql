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

create table IF NOT EXISTS definitions_nodes
(
    id              varchar(255) not null,
    name            varchar(255),
    unique_id       varchar(255),
    type            varchar(255),
    process_id      varchar(255) not null,
    process_version varchar(255) not null,
    primary key (id, process_id, process_version)
);

create table IF NOT EXISTS definitions_nodes_metadata
(
    node_id         varchar(255) not null,
    process_id      varchar(255) not null,
    process_version varchar(255) not null,
    value           varchar(255),
    key             varchar(255) not null,
    primary key (node_id, process_id, process_version, key)
);
alter table if exists definitions_nodes
drop constraint if exists fk_definitions_nodes_definitions
cascade;

alter table if exists definitions_nodes
    add constraint fk_definitions_nodes_definitions
    foreign key (process_id, process_version)
    references definitions
    on
delete
cascade;

alter table if exists definitions_nodes_metadata
drop constraint if exists fk_definitions_nodes_metadata_definitions_nodes
cascade;

alter table if exists definitions_nodes_metadata
    add constraint fk_definitions_nodes_metadata_definitions_nodes
    foreign key (node_id, process_id, process_version)
    references definitions_nodes
    on
delete
cascade;
