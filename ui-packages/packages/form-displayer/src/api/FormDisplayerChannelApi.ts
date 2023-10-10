import { FormOpened } from './types';

export interface FormDisplayerChannelApi {
  notifyOnOpenForm: (opened: FormOpened) => void;
}
