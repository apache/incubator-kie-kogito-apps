/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
}

const FormDisplayerContainer: React.FC<FormDisplayerContainerProps> = ({
  formContent
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
      targetOrigin={window.location.origin}
      envelopePath={'resources/form-displayer.html'}
      formContent={formContent}
      context={buildTestContext(formContent)}
      ref={formDisplayerApiRef}
      key={displayerKey}
    />
  );
};

export default FormDisplayerContainer;
