import React, { useContext } from 'react';
import { FormDetailsGatewayApi } from './FormDetailsGatewayApi';

const FormDetailsContext = React.createContext<FormDetailsGatewayApi>(null);

export const useFormDetailsGatewayApi = (): FormDetailsGatewayApi =>
  useContext<FormDetailsGatewayApi>(FormDetailsContext);

export default FormDetailsContext;
