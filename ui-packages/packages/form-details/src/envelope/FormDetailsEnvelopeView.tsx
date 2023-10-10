import React, { useImperativeHandle, useState } from 'react';
import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import { FormDetailsChannelApi } from '../api';
import FormDetails from './components/FormDetails/FormDetails';
import FormDetailsEnvelopeViewDriver from './FormDetailsEnvelopeViewDriver';
import '@patternfly/patternfly/patternfly.css';
import { FormInfo } from '@kogito-apps/forms-list';
import FormDetailsContextProvider from './components/contexts/FormDetailsContextProvider';

export interface FormDetailsEnvelopeViewApi {
  initialize: (formData?: FormInfo) => void;
}

interface Props {
  channelApi: MessageBusClientApi<FormDetailsChannelApi>;
  targetOrigin: string;
}

export const FormDetailsEnvelopeView = React.forwardRef<
  FormDetailsEnvelopeViewApi,
  Props
>((props, forwardedRef) => {
  const [isEnvelopeConnectedToChannel, setEnvelopeConnectedToChannel] =
    useState<boolean>(false);
  const [formData, setFormData] = useState<FormInfo>(null);
  useImperativeHandle(
    forwardedRef,
    () => ({
      initialize: (form: FormInfo) => {
        setFormData(form);
        setEnvelopeConnectedToChannel(true);
      }
    }),
    []
  );

  return (
    <React.Fragment>
      <FormDetailsContextProvider>
        <FormDetails
          isEnvelopeConnectedToChannel={isEnvelopeConnectedToChannel}
          driver={new FormDetailsEnvelopeViewDriver(props.channelApi)}
          formData={formData}
          targetOrigin={props.targetOrigin}
        />
      </FormDetailsContextProvider>
    </React.Fragment>
  );
});

export default FormDetailsEnvelopeView;
