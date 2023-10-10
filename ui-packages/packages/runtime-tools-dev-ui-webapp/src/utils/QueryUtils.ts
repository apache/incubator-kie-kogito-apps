import _ from 'lodash';
import { User } from '@kogito-apps/consoles-common/dist/environment/auth';
import { SortBy, QueryFilter } from '@kogito-apps/task-inbox';

const createSearchTextArray = (taskNames: string[]) => {
  const formattedTextArray = [];
  taskNames.forEach((word) => {
    formattedTextArray.push({
      referenceName: {
        like: `*${word}*`
      }
    });
  });
  return {
    or: formattedTextArray
  };
};

const createUserAssignmentClause = (currentUser: User) => {
  return {
    or: [
      { actualOwner: { equal: currentUser.id } },
      {
        and: [
          { actualOwner: { isNull: true } },
          {
            not: { excludedUsers: { contains: currentUser.id } }
          },
          {
            or: [
              { potentialUsers: { contains: currentUser.id } },
              { potentialGroups: { containsAny: currentUser.groups } }
            ]
          }
        ]
      }
    ]
  };
};

export const buildTaskInboxWhereArgument = (
  currentUser: User,
  activeFilters: QueryFilter
) => {
  /* istanbul ignore else*/
  if (activeFilters) {
    const filtersClause = [];
    if (activeFilters.taskStates.length > 0) {
      filtersClause.push({
        state: { in: activeFilters.taskStates }
      });
    }
    if (activeFilters.taskNames.length > 0) {
      filtersClause.push(createSearchTextArray(activeFilters.taskNames));
    }

    if (filtersClause.length > 0) {
      return {
        and: [
          createUserAssignmentClause(currentUser),
          {
            and: filtersClause
          }
        ]
      };
    }
  }
  return createUserAssignmentClause(currentUser);
};

export const getOrderByObject = (sortBy: SortBy) => {
  if (!_.isEmpty(sortBy)) {
    return _.set({}, sortBy.property, sortBy.direction.toUpperCase());
  }
  return {
    lastUpdate: 'DESC'
  };
};
