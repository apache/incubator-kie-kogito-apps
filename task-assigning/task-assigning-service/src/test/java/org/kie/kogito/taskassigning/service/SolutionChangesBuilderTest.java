/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.taskassigning.service;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.kogito.taskassigning.core.model.Group;
import org.kie.kogito.taskassigning.core.model.Task;
import org.kie.kogito.taskassigning.core.model.TaskAssigningSolution;
import org.kie.kogito.taskassigning.core.model.TaskAssignment;
import org.kie.kogito.taskassigning.core.model.User;
import org.kie.kogito.taskassigning.core.model.solver.realtime.AddTaskProblemFactChange;
import org.kie.kogito.taskassigning.core.model.solver.realtime.AddUserProblemFactChange;
import org.kie.kogito.taskassigning.core.model.solver.realtime.AssignTaskProblemFactChange;
import org.kie.kogito.taskassigning.core.model.solver.realtime.DisableUserProblemFactChange;
import org.kie.kogito.taskassigning.core.model.solver.realtime.ReleaseTaskProblemFactChange;
import org.kie.kogito.taskassigning.core.model.solver.realtime.RemoveTaskProblemFactChange;
import org.kie.kogito.taskassigning.core.model.solver.realtime.RemoveUserProblemFactChange;
import org.kie.kogito.taskassigning.core.model.solver.realtime.UserPropertyChangeProblemFactChange;
import org.kie.kogito.taskassigning.service.event.UserDataEvent;
import org.kie.kogito.taskassigning.user.service.UserServiceConnector;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.optaplanner.core.api.score.director.ScoreDirector;
import org.optaplanner.core.api.solver.ProblemFactChange;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.kie.kogito.taskassigning.service.TaskState.READY;
import static org.kie.kogito.taskassigning.service.TaskState.RESERVED;
import static org.kie.kogito.taskassigning.service.TestUtil.mockTaskData;
import static org.kie.kogito.taskassigning.service.TestUtil.parseZonedDateTime;
import static org.kie.kogito.taskassigning.service.util.TaskUtil.fromTaskData;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SolutionChangesBuilderTest {

    private static final String TASK_1_ID = "TASK_1_ID";
    private static final ZonedDateTime TASK_1_LAST_UPDATE = parseZonedDateTime("2021-03-11T10:00:00.001Z");

    private static final String USER_1 = "USER_1";
    private static final String USER_2 = "USER_2";

    private static final String GROUP_1 = "GROUP_1";
    private static final String GROUP_2 = "GROUP_2";

    private static final String ATTRIBUTE_1 = "ATTRIBUTE_1";
    private static final String ATTRIBUTE_1_VALUE = "ATTRIBUTE_1_VALUE";
    private static final String ATTRIBUTE_1_VALUE_CHANGED = "ATTRIBUTE_1_VALUE_CHANGED";

    private TaskAssigningServiceContext context;

    @Mock
    private UserServiceConnector userServiceConnector;

    @Mock
    private ScoreDirector<TaskAssigningSolution> scoreDirector;

    @BeforeEach
    void setUp() {
        context = new TaskAssigningServiceContext();
    }

    @Test
    void addNewReadyTaskChange() {
        TaskData taskData = mockTaskData(TASK_1_ID, READY.value(), TASK_1_LAST_UPDATE);
        List<TaskData> taskDataList = mockTaskDataList(taskData);
        TaskAssigningSolution solution = mockSolution(Collections.emptyList(), Collections.emptyList());

        List<ProblemFactChange<TaskAssigningSolution>> result = SolutionChangesBuilder.create()
                .withContext(context)
                .withUserServiceConnector(userServiceConnector)
                .fromTasksData(taskDataList)
                .forSolution(solution)
                .build();

        AddTaskProblemFactChange expected = new AddTaskProblemFactChange(new TaskAssignment(fromTaskData(taskData)));
        assertChangeIsTheChangeSetId(result, 0);
        assertChange(result, 1, expected);
        assertTaskPublishStatus(taskData.getId(), false);
    }

    @Test
    void addNewReservedTaskChangeWithActualOwnerInSolution() {
        TaskData taskData = mockTaskData(TASK_1_ID, TaskState.RESERVED.value(), USER_1, TASK_1_LAST_UPDATE);
        TaskAssigningSolution solution = mockSolution(Collections.singletonList(mockUser(USER_1)), Collections.emptyList());
        addNewReservedTaskChangeWithActualOwner(solution, taskData);
    }

    @Test
    void addNewReservedTaskChangeWithActualOwnerInExternalSystem() {
        TaskData taskData = mockTaskData(TASK_1_ID, TaskState.RESERVED.value(), USER_1, TASK_1_LAST_UPDATE);
        org.kie.kogito.taskassigning.user.service.User externalUser = mockExternalUser(USER_1);
        doReturn(externalUser).when(userServiceConnector).findUser(USER_1);
        TaskAssigningSolution solution = mockSolution(Collections.emptyList(), Collections.emptyList());
        addNewReservedTaskChangeWithActualOwner(solution, taskData);
        verify(userServiceConnector).findUser(USER_1);
    }

    @Test
    void addNewReservedTaskChangeWithActualOwnerNotInExternalSystem() {
        TaskData taskData = mockTaskData(TASK_1_ID, TaskState.RESERVED.value(), USER_1, TASK_1_LAST_UPDATE);
        TaskAssigningSolution solution = mockSolution(Collections.emptyList(), Collections.emptyList());
        addNewReservedTaskChangeWithActualOwner(solution, taskData);
        verify(userServiceConnector).findUser(USER_1);
    }

    private void addNewReservedTaskChangeWithActualOwner(TaskAssigningSolution solution, TaskData taskData) {
        List<TaskData> taskDataList = mockTaskDataList(taskData);
        List<ProblemFactChange<TaskAssigningSolution>> result = SolutionChangesBuilder.create()
                .withContext(context)
                .withUserServiceConnector(userServiceConnector)
                .fromTasksData(taskDataList)
                .forSolution(solution)
                .build();

        AssignTaskProblemFactChange expected = new AssignTaskProblemFactChange(new TaskAssignment(fromTaskData(taskData)), mockUser(USER_1), true);
        assertChangeIsTheChangeSetId(result, 0);
        assertChange(result, 1, expected);
        assertTaskPublishStatus(taskData.getId(), true);
    }

    @Test
    void addReleasedTaskChange() {
        TaskAssignment user1Assignment = new TaskAssignment(mockTask(TASK_1_ID, RESERVED.value()));
        User user1 = TestUtil.mockUser(USER_1, Collections.singletonList(user1Assignment));
        TaskAssigningSolution solution = mockSolution(Collections.singletonList(user1), Collections.singletonList(user1Assignment));
        TaskData taskData = mockTaskData(TASK_1_ID, READY.value(), TASK_1_LAST_UPDATE);

        List<ProblemFactChange<TaskAssigningSolution>> result = SolutionChangesBuilder.create()
                .withContext(context)
                .withUserServiceConnector(userServiceConnector)
                .fromTasksData(mockTaskDataList(taskData))
                .forSolution(solution)
                .build();

        assertChangeIsTheChangeSetId(result, 0);
        assertChange(result, 1, new ReleaseTaskProblemFactChange(new TaskAssignment(fromTaskData(taskData))));
        assertTaskPublishStatus(TASK_1_ID, false);
    }

    @Test
    void addReservedTaskChangeForAnotherUserInSolution() {
        TaskAssignment user1Assignment = new TaskAssignment(mockTask(TASK_1_ID));
        User user1 = TestUtil.mockUser(USER_1, Collections.singletonList(user1Assignment));
        User user2 = mockUser(USER_2);
        TaskAssigningSolution solution = mockSolution(Arrays.asList(user1, user2), Collections.singletonList(user1Assignment));
        TaskData taskData = mockTaskData(TASK_1_ID, RESERVED.value(), USER_2, TASK_1_LAST_UPDATE);
        addReservedTaskChangeForAnotherUser(solution, taskData, user2);
    }

    @Test
    void addReservedTaskChangeForAnotherUserInExternalSystem() {
        TaskAssignment user1Assignment = new TaskAssignment(mockTask(TASK_1_ID));
        User user1 = TestUtil.mockUser(USER_1, Collections.singletonList(user1Assignment));
        org.kie.kogito.taskassigning.user.service.User user2 = mockExternalUser(USER_2);
        doReturn(user2).when(userServiceConnector).findUser(USER_2);
        TaskAssigningSolution solution = mockSolution(Collections.singletonList(user1), Collections.singletonList(user1Assignment));
        TaskData taskData = mockTaskData(TASK_1_ID, RESERVED.value(), USER_2, TASK_1_LAST_UPDATE);
        addReservedTaskChangeForAnotherUser(solution, taskData, mockUser(USER_2));
        verify(userServiceConnector).findUser(USER_2);
    }

    @Test
    void addReservedTaskChangeForAnotherUserNotInExternalSystem() {
        TaskAssignment user1Assignment = new TaskAssignment(mockTask(TASK_1_ID));
        User user1 = TestUtil.mockUser(USER_1, Collections.singletonList(user1Assignment));
        TaskAssigningSolution solution = mockSolution(Collections.singletonList(user1), Collections.singletonList(user1Assignment));
        TaskData taskData = mockTaskData(TASK_1_ID, RESERVED.value(), USER_2, TASK_1_LAST_UPDATE);
        addReservedTaskChangeForAnotherUser(solution, taskData, mockUser(USER_2));
        verify(userServiceConnector).findUser(USER_2);
    }

    private void addReservedTaskChangeForAnotherUser(TaskAssigningSolution solution, TaskData taskData, User user) {
        List<ProblemFactChange<TaskAssigningSolution>> result = SolutionChangesBuilder.create()
                .withContext(context)
                .withUserServiceConnector(userServiceConnector)
                .fromTasksData(mockTaskDataList(taskData))
                .forSolution(solution)
                .build();

        assertChangeIsTheChangeSetId(result, 0);
        assertChange(result, 1, new AssignTaskProblemFactChange(new TaskAssignment(fromTaskData(taskData)), user, true));
        assertTaskPublishStatus(TASK_1_ID, true);
    }

    @Test
    void addReservedTaskChangeForSameUserButNotPinned() {
        TaskAssignment user1Assignment = new TaskAssignment(mockTask(TASK_1_ID));
        User user1 = TestUtil.mockUser(USER_1, Collections.singletonList(user1Assignment));
        user1Assignment.setPinned(false);

        TaskAssigningSolution solution = mockSolution(Collections.singletonList(user1), Collections.singletonList(user1Assignment));
        TaskData taskData = mockTaskData(TASK_1_ID, RESERVED.value(), USER_1, TASK_1_LAST_UPDATE);
        addReservedTaskChangeForAnotherUser(solution, taskData, user1);
    }

    @Test
    void addRemoveTaskAborted() {
        addRemoveTaskInTerminalStatus(TaskState.ABORTED);
    }

    @Test
    void addRemoveTaskCompleted() {
        addRemoveTaskInTerminalStatus(TaskState.COMPLETED);
    }

    @Test
    void addRemoveTaskSkipped() {
        addRemoveTaskInTerminalStatus(TaskState.SKIPPED);
    }

    void addRemoveTaskInTerminalStatus(TaskState terminalState) {
        TaskAssignment user1Assignment = new TaskAssignment(mockTask(TASK_1_ID));
        User user1 = TestUtil.mockUser(USER_1, Collections.singletonList(user1Assignment));
        TaskAssigningSolution solution = mockSolution(Collections.singletonList(user1), Collections.singletonList(user1Assignment));

        TaskData taskData = mockTaskData(TASK_1_ID, terminalState.value(), TASK_1_LAST_UPDATE);

        List<ProblemFactChange<TaskAssigningSolution>> result = SolutionChangesBuilder.create()
                .withContext(context)
                .withUserServiceConnector(userServiceConnector)
                .fromTasksData(mockTaskDataList(taskData))
                .forSolution(solution)
                .build();

        assertChangeIsTheChangeSetId(result, 0);
        assertChange(result, 1, new RemoveTaskProblemFactChange(new TaskAssignment(fromTaskData(taskData))));
    }

    @Test
    void addNewUserChange() {
        org.kie.kogito.taskassigning.user.service.User newExternalUser = mockExternalUser(USER_1);
        User newUser = mockUser(USER_1);
        TaskAssigningSolution solution = mockSolution(Collections.emptyList(), Collections.emptyList());
        UserDataEvent event = new UserDataEvent(Collections.singletonList(newExternalUser), ZonedDateTime.now());

        List<ProblemFactChange<TaskAssigningSolution>> result = SolutionChangesBuilder.create()
                .withContext(context)
                .withUserServiceConnector(userServiceConnector)
                .fromTasksData(Collections.emptyList())
                .fromUserDataEvent(event)
                .forSolution(solution)
                .build();

        AddUserProblemFactChange expected = new AddUserProblemFactChange(newUser);
        assertChangeIsTheChangeSetId(result, 0);
        assertChange(result, 1, expected);
    }

    @Test
    void addUpdateUserChangeByGroup() {
        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put(ATTRIBUTE_1, ATTRIBUTE_1_VALUE);
        User user = mockUser(USER_1, Collections.singleton(new Group(GROUP_1)), userAttributes);
        Map<String, Object> externalUserAttributes = new HashMap<>();
        externalUserAttributes.put(ATTRIBUTE_1, ATTRIBUTE_1_VALUE);
        org.kie.kogito.taskassigning.user.service.User updatedExternalUser = mockExternalUser(USER_1,
                Collections.singleton(mockExternalGroup(GROUP_2)),
                externalUserAttributes);

        addUpdateUserChange(user, updatedExternalUser);
    }

    @Test
    void addUpdateUserChangeByAttribute() {
        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put(ATTRIBUTE_1, ATTRIBUTE_1_VALUE);
        User user = mockUser(USER_1, Collections.singleton(new Group(GROUP_1)), userAttributes);
        Map<String, Object> externalUserAttributes = new HashMap<>();
        externalUserAttributes.put(ATTRIBUTE_1, ATTRIBUTE_1_VALUE_CHANGED);
        org.kie.kogito.taskassigning.user.service.User updatedExternalUser = mockExternalUser(USER_1,
                Collections.singleton(mockExternalGroup(GROUP_1)),
                externalUserAttributes);

        addUpdateUserChange(user, updatedExternalUser);
    }

    private void addUpdateUserChange(User user, org.kie.kogito.taskassigning.user.service.User updatedExternalUser) {
        TaskAssigningSolution solution = mockSolution(Collections.singletonList(user), Collections.emptyList());
        UserDataEvent event = new UserDataEvent(Collections.singletonList(updatedExternalUser), ZonedDateTime.now());

        List<ProblemFactChange<TaskAssigningSolution>> result = SolutionChangesBuilder.create()
                .withContext(context)
                .withUserServiceConnector(userServiceConnector)
                .fromTasksData(Collections.emptyList())
                .fromUserDataEvent(event)
                .forSolution(solution)
                .build();

        Set<Group> expectedGroups = updatedExternalUser.getGroups().stream()
                .map(externalGroup -> new Group(externalGroup.getId()))
                .collect(Collectors.toSet());
        UserPropertyChangeProblemFactChange expected = new UserPropertyChangeProblemFactChange(user,
                true,
                updatedExternalUser.getAttributes(),
                expectedGroups);
        assertChangeIsTheChangeSetId(result, 0);
        assertChange(result, 1, expected);
    }

    @Test
    void addDisableUserChange() {
        User user = mockUser(USER_1);
        user.setEnabled(true);
        TaskAssigningSolution solution = mockSolution(Collections.singletonList(user), Collections.emptyList());
        UserDataEvent event = new UserDataEvent(Collections.emptyList(), ZonedDateTime.now());

        List<ProblemFactChange<TaskAssigningSolution>> result = SolutionChangesBuilder.create()
                .withContext(context)
                .withUserServiceConnector(userServiceConnector)
                .fromTasksData(Collections.emptyList())
                .fromUserDataEvent(event)
                .forSolution(solution)
                .build();

        DisableUserProblemFactChange expected = new DisableUserProblemFactChange(user);
        assertChangeIsTheChangeSetId(result, 0);
        assertChange(result, 1, expected);
    }

    @Test
    void addRemoveUserChange() {
        User user = mockUser(USER_1);
        user.setEnabled(false);
        TaskAssigningSolution solution = mockSolution(Collections.singletonList(user), Collections.emptyList());

        List<ProblemFactChange<TaskAssigningSolution>> result = SolutionChangesBuilder.create()
                .withContext(context)
                .withUserServiceConnector(userServiceConnector)
                .fromTasksData(Collections.emptyList())
                .forSolution(solution)
                .build();

        RemoveUserProblemFactChange expected = new RemoveUserProblemFactChange(user);
        assertChangeIsTheChangeSetId(result, 0);
        assertChange(result, 1, expected);
    }

    private static TaskAssigningSolution mockSolution(List<User> users, List<TaskAssignment> task) {
        return new TaskAssigningSolution("1", users, task);
    }

    private static List<TaskData> mockTaskDataList(TaskData... tasks) {
        return Arrays.asList(tasks);
    }

    private static User mockUser(String userId) {
        return new User(userId);
    }

    private static User mockUser(String userId, Set<Group> groups, Map<String, Object> attributes) {
        return new User(userId, true, groups, attributes);
    }

    private static Task mockTask(String taskId, String state) {
        return Task.newBuilder()
                .id(taskId)
                .state(state)
                .build();
    }

    private static Task mockTask(String taskId) {
        return Task.newBuilder()
                .id(taskId)
                .build();
    }

    private static org.kie.kogito.taskassigning.user.service.User mockExternalUser(String userId) {
        return mockExternalUser(userId, Collections.emptySet(), Collections.emptyMap());
    }

    private static org.kie.kogito.taskassigning.user.service.User mockExternalUser(String userId,
            Set<org.kie.kogito.taskassigning.user.service.Group> groups,
            Map<String, Object> attributes) {
        org.kie.kogito.taskassigning.user.service.User result = mock(org.kie.kogito.taskassigning.user.service.User.class);
        doReturn(userId).when(result).getId();
        doReturn(groups).when(result).getGroups();
        doReturn(attributes).when(result).getAttributes();
        return result;
    }

    private static org.kie.kogito.taskassigning.user.service.Group mockExternalGroup(String groupId) {
        org.kie.kogito.taskassigning.user.service.Group result = mock(org.kie.kogito.taskassigning.user.service.Group.class);
        doReturn(groupId).when(result).getId();
        return result;
    }

    private void assertChangeIsTheChangeSetId(List<ProblemFactChange<TaskAssigningSolution>> result, int index) {
        long currentChangeSetId = context.getCurrentChangeSetId();
        result.get(index).doChange(scoreDirector);
        assertThat(context.getCurrentChangeSetId()).isEqualTo(currentChangeSetId + 1);
    }

    private static void assertChange(List<ProblemFactChange<TaskAssigningSolution>> result, int index, AddTaskProblemFactChange expected) {
        assertThat(result.get(index)).isInstanceOf(AddTaskProblemFactChange.class);
        AddTaskProblemFactChange change = (AddTaskProblemFactChange) result.get(index);
        assertTaskEquals(expected.getTaskAssignment(), change.getTaskAssignment());
    }

    private static void assertChange(List<ProblemFactChange<TaskAssigningSolution>> result, int index, AssignTaskProblemFactChange expected) {
        assertThat(result.get(index)).isInstanceOf(AssignTaskProblemFactChange.class);
        AssignTaskProblemFactChange change = (AssignTaskProblemFactChange) result.get(index);
        assertTaskEquals(expected.getTaskAssignment(), change.getTaskAssignment());
    }

    private static void assertChange(List<ProblemFactChange<TaskAssigningSolution>> result, int index, ReleaseTaskProblemFactChange expected) {
        assertThat(result.get(index)).isInstanceOf(ReleaseTaskProblemFactChange.class);
        ReleaseTaskProblemFactChange change = (ReleaseTaskProblemFactChange) result.get(index);
        assertTaskEquals(expected.getTaskAssignment(), change.getTaskAssignment());
    }

    private static void assertChange(List<ProblemFactChange<TaskAssigningSolution>> result, int index, RemoveTaskProblemFactChange expected) {
        assertThat(result.get(index)).isInstanceOf(RemoveTaskProblemFactChange.class);
        RemoveTaskProblemFactChange change = (RemoveTaskProblemFactChange) result.get(index);
        assertTaskEquals(expected.getTaskAssignment(), change.getTaskAssignment());
    }

    private static void assertChange(List<ProblemFactChange<TaskAssigningSolution>> result, int index, AddUserProblemFactChange expected) {
        assertThat(result.get(index)).isInstanceOf(AddUserProblemFactChange.class);
        AddUserProblemFactChange change = (AddUserProblemFactChange) result.get(index);
        assertUserEquals(change.getUser(), expected.getUser());
    }

    private static void assertChange(List<ProblemFactChange<TaskAssigningSolution>> result, int index, UserPropertyChangeProblemFactChange expected) {
        assertThat(result.get(index)).isInstanceOf(UserPropertyChangeProblemFactChange.class);
        UserPropertyChangeProblemFactChange change = (UserPropertyChangeProblemFactChange) result.get(index);
        assertUserEquals(change.getUser(), expected.getUser());
        assertThat(change.getNewAttributes()).isEqualTo(expected.getNewAttributes());
        assertThat(change.getNewGroups()).isEqualTo(expected.getNewGroups());
    }

    private static void assertChange(List<ProblemFactChange<TaskAssigningSolution>> result, int index, DisableUserProblemFactChange expected) {
        assertThat(result.get(index)).isInstanceOf(DisableUserProblemFactChange.class);
        DisableUserProblemFactChange change = (DisableUserProblemFactChange) result.get(index);
        assertUserEquals(change.getUser(), expected.getUser());
    }

    private static void assertChange(List<ProblemFactChange<TaskAssigningSolution>> result, int index, RemoveUserProblemFactChange expected) {
        assertThat(result.get(index)).isInstanceOf(RemoveUserProblemFactChange.class);
        RemoveUserProblemFactChange change = (RemoveUserProblemFactChange) result.get(index);
        assertUserEquals(change.getUser(), expected.getUser());
    }

    private static void assertTaskEquals(TaskAssignment t1, TaskAssignment t2) {
        assertThat(t1.getId()).isEqualTo(t2.getId());
    }

    private static void assertUserEquals(User u1, User u2) {
        assertThat(u1.getId()).isEqualTo(u2.getId());
    }

    private void assertTaskPublishStatus(String taskId, boolean published) {
        assertThat(context.isTaskPublished(taskId)).isEqualTo(published);
    }

}
