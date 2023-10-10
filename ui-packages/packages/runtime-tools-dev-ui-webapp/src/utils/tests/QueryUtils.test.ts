import { getOrderByObject, buildTaskInboxWhereArgument } from '../QueryUtils';

describe('QueryUtils test', () => {
  it('getOrderByObject', () => {
    const sortBy: any = {
      direction: 'desc',
      property: 'lastUpdate'
    };
    const result = getOrderByObject(sortBy);

    expect(result).toEqual({ lastUpdate: 'DESC' });
  });

  it('getOrderByObject - empty sortBy', () => {
    const sortBy: any = {};
    const result = getOrderByObject(sortBy);

    expect(result).toEqual({ lastUpdate: 'DESC' });
  });
  it('buildTaskInboxWhereArgument', () => {
    const currentUser = { id: '', groups: [] };
    const activeFilters = {
      taskNames: [],
      taskStates: ['Ready, Reserved']
    };

    const expectedResult = {
      and: [
        {
          or: [
            {
              actualOwner: { equal: '' }
            },
            {
              and: [
                {
                  actualOwner: { isNull: true }
                },
                {
                  not: { excludedUsers: { contains: '' } }
                },
                {
                  or: [
                    { potentialUsers: { contains: '' } },
                    { potentialGroups: { containsAny: [] } }
                  ]
                }
              ]
            }
          ]
        },
        {
          and: [
            {
              state: {
                in: ['Ready, Reserved']
              }
            }
          ]
        }
      ]
    };
    const result = buildTaskInboxWhereArgument(currentUser, activeFilters);
    expect(result).toEqual(expectedResult);
  });

  it('buildTaskInboxWhereArgument - with empty taskStates', () => {
    const currentUser = { id: '', groups: [] };
    const activeFilters = {
      taskNames: [],
      taskStates: []
    };

    const expectedResult = {
      or: [
        {
          actualOwner: { equal: '' }
        },
        {
          and: [
            {
              actualOwner: { isNull: true }
            },
            {
              not: { excludedUsers: { contains: '' } }
            },
            {
              or: [
                { potentialUsers: { contains: '' } },
                { potentialGroups: { containsAny: [] } }
              ]
            }
          ]
        }
      ]
    };
    const result = buildTaskInboxWhereArgument(currentUser, activeFilters);
    expect(result).toEqual(expectedResult);
  });

  it('buildTaskInboxWhereArgument- with taskName', () => {
    const currentUser = { id: '', groups: [] };
    const activeFilters = {
      taskNames: ['test'],
      taskStates: ['Ready, Reserved']
    };

    const expectedResult = {
      and: [
        {
          or: [
            {
              actualOwner: { equal: '' }
            },
            {
              and: [
                {
                  actualOwner: { isNull: true }
                },
                {
                  not: { excludedUsers: { contains: '' } }
                },
                {
                  or: [
                    { potentialUsers: { contains: '' } },
                    { potentialGroups: { containsAny: [] } }
                  ]
                }
              ]
            }
          ]
        },
        {
          and: [
            {
              state: {
                in: ['Ready, Reserved']
              }
            },
            {
              or: [
                {
                  referenceName: {
                    like: '*test*'
                  }
                }
              ]
            }
          ]
        }
      ]
    };
    const result = buildTaskInboxWhereArgument(currentUser, activeFilters);
    expect(result).toEqual(expectedResult);
  });
});
