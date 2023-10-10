import React from 'react';
import { FormEditorProps } from '../FormEditor';
import { ResizableContent } from '../../FormDetails/FormDetails';
import { OUIAProps } from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';

const MockedFormEditor = React.forwardRef<
  ResizableContent,
  FormEditorProps & OUIAProps
>((props, ref) => {
  return <></>;
});

export default MockedFormEditor;
