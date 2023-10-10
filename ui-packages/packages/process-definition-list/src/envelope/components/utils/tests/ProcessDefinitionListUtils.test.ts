import { getColumn, getActionColumn } from '../ProcessDefinitionListUtils';

describe('Utils tests', () => {
  it('Test default column', () => {
    const column = getColumn('path', 'Label');

    expect(column).not.toBeNull();
    expect(column.path).toBe('path');
    expect(column.label).toBe('Label');
    expect(column.bodyCellTransformer).not.toBeUndefined();
  });

  it('Test action column', () => {
    const column = getActionColumn(jest.fn(), 'Workflow');

    expect(column).not.toBeNull();
    expect(column.path).toBe('actions');
    expect(column.label).toBe('Actions');
    expect(column.bodyCellTransformer).not.toBeUndefined();
  });
});
