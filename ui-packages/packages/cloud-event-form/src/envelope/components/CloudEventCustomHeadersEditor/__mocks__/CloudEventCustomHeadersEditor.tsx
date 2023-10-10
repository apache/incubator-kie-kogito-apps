import React from 'react';
import { CloudEventCustomHeadersEditorApi } from '../CloudEventCustomHeadersEditor';
import { OUIAProps } from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';

const MockedCloudEventCustomHeadersEditor = React.forwardRef<
  CloudEventCustomHeadersEditorApi,
  OUIAProps
>((props, ref) => {
  return <></>;
});

export default MockedCloudEventCustomHeadersEditor;
