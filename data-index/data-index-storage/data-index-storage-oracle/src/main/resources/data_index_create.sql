    create table JobEntity (
       id varchar(255) not null,
        callbackEndpoint varchar(255),
        endpoint varchar(255),
        executionCounter NUMBER(10),
        expirationTime timestamp,
        lastUpdate timestamp,
        nodeInstanceId varchar(255),
        priority NUMBER(10),
        processId varchar(255),
        processInstanceId varchar(255),
        repeatInterval NUMBER(19),
        repeatLimit NUMBER(10),
        retries NUMBER(10),
        rootProcessId varchar(255),
        rootProcessInstanceId varchar(255),
        scheduledId varchar(255),
        status varchar(255),
        CONSTRAINT JobEntity_primary PRIMARY KEY(id)
    );

    create table MilestoneEntity (
        id varchar(255) not null,
        name varchar(255),
        status varchar(255),
        CONSTRAINT MilestoneEntity_primary PRIMARY KEY(id)
    );

    create table NodeInstanceEntity (
        id varchar(255) not null,
        definitionId varchar(255),
        enter timestamp,
        exit timestamp,
        name varchar(255),
        nodeId varchar(255),
        type varchar(255),
        CONSTRAINT NodeInstanceEntity_primary PRIMARY KEY(id)
    );

    create table ProcessInstanceEntity (
        id varchar(255) not null,
        businessKey varchar(255),
        end_time timestamp,
        endpoint varchar(255),
        message varchar(255),
        nodeDefinitionId varchar(255),
        last_update_time timestamp,
        parentProcessInstanceId varchar(255),
        processId varchar(255),
        processName varchar(255),
        rootProcessId varchar(255),
        rootProcessInstanceId varchar(255),
        start_time timestamp,
        state NUMBER(10),
        variables clob,
        CONSTRAINT ProcessInstanceEntity_primary PRIMARY KEY(id),
        CONSTRAINT variables_json CHECK (variables IS JSON)
    );

    create table ProcessInstanceEntity_addons (
       ProcessInstanceEntity_id varchar(255) not null,
        addons varchar(255),
       CONSTRAINT FKsdc13xvts9tdmimek9pfei5up FOREIGN KEY (ProcessInstanceEntity_id)
       		REFERENCES ProcessInstanceEntity(id)
       		ON DELETE CASCADE
    );

    create table ProcessInstanceEntity_MilestoneEntity (
       ProcessInstanceEntity_id varchar(255) not null,
       milestones_id varchar(255) not NULL,
       CONSTRAINT UK_iw2hpwwogyfuwe1oss9oqar93 UNIQUE (milestones_id),
       CONSTRAINT FKkk3pdt6yntyad4257jbcul7xk FOREIGN KEY (milestones_id) REFERENCES MilestoneEntity(id),
       CONSTRAINT FK8frxihvalnxacv1vcdbrt4rwa FOREIGN KEY (ProcessInstanceEntity_id) REFERENCES ProcessInstanceEntity(id)
    );

    create table ProcessInstanceEntity_NodeInstanceEntity (
       ProcessInstanceEntity_id varchar(255) not null,
       nodes_id varchar(255) not NULL,
       CONSTRAINT UK_sdve1m52p29bajp0ui95er5qj UNIQUE (nodes_id),
       CONSTRAINT FKrsiysusml360wxgiqkfjbuftg FOREIGN KEY (nodes_id) REFERENCES NodeInstanceEntity(id),
       CONSTRAINT FK2w1s1fhpoaeighh1og96q24dn FOREIGN KEY (ProcessInstanceEntity_id) REFERENCES ProcessInstanceEntity(id)
    );

    create table ProcessInstanceEntity_roles (
       ProcessInstanceEntity_id varchar(255) not null,
        roles varchar(255),
       CONSTRAINT FK2cwq2idof87vgrg6wy5ng15h2 FOREIGN KEY (ProcessInstanceEntity_id)
       		REFERENCES ProcessInstanceEntity(id)
       		ON DELETE CASCADE
    );

    create table UserTaskInstanceEntity (
       id varchar(255) not null,
        actualOwner varchar(255),
        completed timestamp,
        description varchar(255),
        endpoint varchar(255),
        inputs clob,
        lastUpdate timestamp,
        name varchar(255),
        outputs clob,
        priority varchar(255),
        processId varchar(255),
        processInstanceId varchar(255),
        referenceName varchar(255),
        rootProcessId varchar(255),
        rootProcessInstanceId varchar(255),
        started timestamp,
        state varchar(255),
        CONSTRAINT UserTaskInstanceEntity_primary PRIMARY KEY(id),
        CONSTRAINT inputs_json CHECK (inputs IS JSON),
        CONSTRAINT outputs_json CHECK (outputs IS JSON)
    );

    create table UserTaskInstanceEntity_adminGroups (
       UserTaskInstanceEntity_id varchar(255) not null,
       adminGroups varchar(255),
       CONSTRAINT FKae51g1f8eyy6695mdecdi3bjd FOREIGN KEY (UserTaskInstanceEntity_id)
       		REFERENCES UserTaskInstanceEntity(id)
       		ON DELETE CASCADE
    );

    create table UserTaskInstanceEntity_adminUsers (
       UserTaskInstanceEntity_id varchar(255) not null,
       adminUsers varchar(255),
       CONSTRAINT FK142t43lxnp57eq9wiwtlayat4 FOREIGN KEY (UserTaskInstanceEntity_id)
       		REFERENCES UserTaskInstanceEntity(id)
       		ON DELETE CASCADE
    );

    create table UserTaskInstanceEntity_excludedUsers (
       UserTaskInstanceEntity_id varchar(255) not null,
       excludedUsers varchar(255),
       CONSTRAINT FKl3j5dxg2be39bc8rqtxp5stvj FOREIGN KEY (UserTaskInstanceEntity_id)
       		REFERENCES UserTaskInstanceEntity(id)
       		ON DELETE CASCADE
    );

    create table UserTaskInstanceEntity_potentialGroups (
       UserTaskInstanceEntity_id varchar(255) not null,
        potentialGroups varchar(255),
       CONSTRAINT FKr1s3ekcbpoe38m13jb680qoo8 FOREIGN KEY (UserTaskInstanceEntity_id)
       		REFERENCES UserTaskInstanceEntity(id)
       		ON DELETE CASCADE
    );

    create table UserTaskInstanceEntity_potentialUsers (
       UserTaskInstanceEntity_id varchar(255) not null,
       potentialUsers varchar(255),
       CONSTRAINT FK472u4nopslxglrwxldys5axje FOREIGN KEY (UserTaskInstanceEntity_id)
       		REFERENCES UserTaskInstanceEntity(id)
       		ON DELETE CASCADE
    );

    create table CommentEntity (
       id varchar(255) not null,
       content varchar(255),
       updatedAt timestamp,
       updatedBy varchar(255),
	   CONSTRAINT CommentEntity_primary PRIMARY KEY(id)
    );

    create table UserTaskInstanceEntity_CommentEntity (
       UserTaskInstanceEntity_id varchar(255) not null,
       comments_id varchar(255) not NULL,
       CONSTRAINT UserTaskInstanceEntity_CommentEntity UNIQUE (comments_id),
       CONSTRAINT UserTaskInstanceEntity_CommentEntity_commentId FOREIGN KEY (comments_id)
       		REFERENCES CommentEntity(id)
       		ON DELETE CASCADE,
       CONSTRAINT UserTaskInstanceEntity_CommentEntity_id FOREIGN KEY (UserTaskInstanceEntity_id)
       		REFERENCES UserTaskInstanceEntity(id)
       		ON DELETE CASCADE
    );

    create table AttachmentEntity (
       id varchar(255) not null,
       name varchar(255),
       content varchar(255),
       updatedAt timestamp,
       updatedBy varchar(255),
	   CONSTRAINT AttachmentEntity_primary PRIMARY KEY(id)

    );

    create table UserTaskInstanceEntity_AttachmentEntity(
        UserTaskInstanceEntity_id varchar(255) not null,
        attachments_id varchar(255) not NULL,
        CONSTRAINT UserTaskInstanceEntity_AttachmentEntity_pk UNIQUE (attachments_id),
        CONSTRAINT UserTaskInstanceEntity_AttachmentEntity_attachmentId FOREIGN KEY (attachments_id)
       		REFERENCES AttachmentEntity(id)
       		ON DELETE CASCADE,
        CONSTRAINT UserTaskInstanceEntity_AttachmentEntity_id FOREIGN KEY (UserTaskInstanceEntity_id)
       		REFERENCES UserTaskInstanceEntity(id)
       		ON DELETE CASCADE
    );
