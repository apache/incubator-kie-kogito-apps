import { ProcessInstanceFilter } from '@kogito-apps/management-console-shared/dist/types';

const formatSearchWords = (searchWords: string[]) => {
  const tempSearchWordsArray = [];
  searchWords.forEach((word) => {
    tempSearchWordsArray.push({ businessKey: { like: word } });
  });
  return tempSearchWordsArray;
};

export const buildProcessListWhereArgument = (
  filters: ProcessInstanceFilter
) => {
  if (filters.businessKey.length === 0) {
    return {
      parentProcessInstanceId: { isNull: true },
      state: { in: filters.status }
    };
  } else {
    return {
      parentProcessInstanceId: { isNull: true },
      state: { in: filters.status },
      or: formatSearchWords(filters.businessKey)
    };
  }
};
