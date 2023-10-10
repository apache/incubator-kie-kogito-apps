import React, { useEffect } from 'react';
import { OUIAProps } from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { EmbeddedFormsList, FormInfo } from '@kogito-apps/forms-list';
import { FormsListGatewayApi } from '../../../channel/FormsList';
import { useFormsListGatewayApi } from '../../../channel/FormsList/FormsListContext';
import { useHistory } from 'react-router-dom';
import { useDevUIAppContext } from '../../contexts/DevUIAppContext';

const FormsListContainer: React.FC<OUIAProps> = () => {
  const history = useHistory();
  const gatewayApi: FormsListGatewayApi = useFormsListGatewayApi();
  const appContext = useDevUIAppContext();

  useEffect(() => {
    const unsubscriber = gatewayApi.onOpenFormListen({
      onOpen(formData: FormInfo) {
        history.push({
          pathname: `/Forms/${formData.name}`,
          state: {
            filter: gatewayApi.getFormFilter(),
            formData: formData
          }
        });
      }
    });
    return () => {
      unsubscriber.unSubscribe();
    };
  }, []);

  return (
    <EmbeddedFormsList
      driver={gatewayApi}
      targetOrigin={appContext.getDevUIUrl()}
    />
  );
};

export default FormsListContainer;
