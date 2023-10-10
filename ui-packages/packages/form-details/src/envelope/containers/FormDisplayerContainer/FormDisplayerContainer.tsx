import React, { useEffect, useState } from 'react';
import uuidv4 from 'uuid';
import {
  EmbeddedFormDisplayer,
  FormDisplayerApi
} from '@kogito-apps/form-displayer';
import { Form } from '../../../api';
import { useFormDetailsContext } from '../../components/contexts/FormDetailsContext';
import { buildTestContext } from './utils/utils';

interface FormDisplayerContainerProps {
  formContent: Form;
  targetOrigin: string;
}

const FormDisplayerContainer: React.FC<FormDisplayerContainerProps> = ({
  formContent,
  targetOrigin
}) => {
  const [displayerKey, setDisplayerKey] = useState<string>(uuidv4());
  const appContext = useFormDetailsContext();
  const formDisplayerApiRef = React.useRef<FormDisplayerApi>();

  useEffect(() => {
    const unsubscribeUserChange = appContext.onUpdateContent({
      onUpdateContent(formContent) {
        setDisplayerKey(uuidv4());
      }
    });
    return () => {
      unsubscribeUserChange.unSubscribe();
    };
  }, []);

  return (
    <EmbeddedFormDisplayer
      targetOrigin={targetOrigin}
      envelopePath={'resources/form-displayer.html'}
      formContent={formContent}
      context={buildTestContext(formContent)}
      ref={formDisplayerApiRef}
      key={displayerKey}
    />
  );
};

export default FormDisplayerContainer;
