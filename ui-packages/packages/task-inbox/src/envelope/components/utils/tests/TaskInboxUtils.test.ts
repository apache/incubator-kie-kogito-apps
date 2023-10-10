import {
  getDateColumn,
  getDefaultColumn,
  getTaskDescriptionColumn,
  getTaskStateColumn
} from '../TaskInboxUtils';

describe('Utils tests', () => {
  it('Test default column', () => {
    const column = getDefaultColumn('path', 'Label', true);

    expect(column).not.toBeNull();
    expect(column.path).toBe('path');
    expect(column.label).toBe('Label');
    expect(column.bodyCellTransformer).toBeUndefined();
  });

  it('Test task description column', () => {
    const column = getDateColumn('path', 'Date Column');

    expect(column).not.toBeNull();
    expect(column.path).toBe('path');
    expect(column.label).toBe('Date Column');
    expect(column.bodyCellTransformer).not.toBeUndefined();
  });

  it('Test task description column', () => {
    const column = getTaskDescriptionColumn(jest.fn());

    expect(column).not.toBeNull();
    expect(column.path).toBe('referenceName');
    expect(column.label).toBe('Name');
    expect(column.bodyCellTransformer).not.toBeUndefined();
  });

  it('Test task state column', () => {
    const column = getTaskStateColumn();

    expect(column).not.toBeNull();
    expect(column.path).toBe('state');
    expect(column.label).toBe('Status');
    expect(column.bodyCellTransformer).not.toBeUndefined();
  });
});
