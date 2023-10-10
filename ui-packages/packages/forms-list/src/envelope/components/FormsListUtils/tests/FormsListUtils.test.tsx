import { FormType } from '../../../../api/FormsListEnvelopeApi';
import {
  getDateColumn,
  getFormNameColumn,
  getFormTypeColumn,
  getFormTypeLabel
} from '../FormsListUtils';

describe('forms list utils tests', () => {
  it('get form name column', () => {
    const result = getFormNameColumn(jest.fn());
    expect(result.label).toEqual('Name');
    expect(result.path).toEqual('name');
  });
  it('get date column', () => {
    const result = getDateColumn('lastModified', 'Last Modified');
    expect(result.label).toEqual('Last Modified');
    expect(result.path).toEqual('lastModified');
  });
  it('get form type', () => {
    const result = getFormTypeColumn();
    expect(result.label).toEqual('Type');
    expect(result.path).toEqual('type');
  });
  it('get form lable', () => {
    const result1 = getFormTypeLabel(FormType.HTML);
    expect(result1.props.children).toEqual(FormType.HTML);
    const result2 = getFormTypeLabel(FormType.TSX);
    expect(result2.props.children).toEqual('REACT');
  });
});
