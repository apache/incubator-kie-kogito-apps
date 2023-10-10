import React, { useEffect, useState } from 'react';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { ProcessDefinition, ProcessFormDriver } from '../../../api';
import { KogitoSpinner } from '@kogito-apps/components-common/dist/components/KogitoSpinner';
import { ServerErrors } from '@kogito-apps/components-common/dist/components/ServerErrors';
import { FormRenderer } from '@kogito-apps/components-common/dist/components/FormRenderer';
import { FormRendererApi } from '@kogito-apps/components-common/dist/types';
import { FormAction } from '@kogito-apps/components-common/dist/components/utils';
import { Bullseye } from '@patternfly/react-core/dist/js/layouts/Bullseye';
export interface ProcessFormProps {
  processDefinition: ProcessDefinition;
  driver: ProcessFormDriver;
  isEnvelopeConnectedToChannel: boolean;
}

const ProcessForm: React.FC<ProcessFormProps & OUIAProps> = ({
  processDefinition,
  driver,
  isEnvelopeConnectedToChannel,
  ouiaId,
  ouiaSafe
}) => {
  const formRendererApi = React.useRef<FormRendererApi>();
  const [processFormSchema, setProcessFormSchema] = useState<any>({});
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string>(null);

  const formAction: FormAction[] = [
    {
      name: 'Start'
    }
  ];

  useEffect(() => {
    if (!isEnvelopeConnectedToChannel) {
      setIsLoading(true);
      return;
    }
    init();
  }, [isEnvelopeConnectedToChannel]);

  const init = async (): Promise<void> => {
    try {
      const schema = await driver.getProcessFormSchema(processDefinition);
      setProcessFormSchema(schema);
    } catch (errorContent) {
      setError(errorContent);
    } finally {
      setIsLoading(false);
    }
  };

  const onSubmit = (value: any): void => {
    driver.startProcess(value).then(() => {
      formRendererApi?.current?.doReset();
    });
  };

  if (isLoading) {
    return (
      <Bullseye>
        <KogitoSpinner
          spinnerText="Loading process forms..."
          ouiaId="process-form-loading"
        />
      </Bullseye>
    );
  }

  if (error) {
    return <ServerErrors error={error} variant={'large'} />;
  }

  return (
    <div
      {...componentOuiaProps(
        ouiaId,
        'process-form',
        ouiaSafe ? ouiaSafe : !isLoading
      )}
    >
      <FormRenderer
        formSchema={processFormSchema}
        model={{}}
        readOnly={false}
        onSubmit={onSubmit}
        formActions={formAction}
        ref={formRendererApi}
      />
    </div>
  );
};

export default ProcessForm;
