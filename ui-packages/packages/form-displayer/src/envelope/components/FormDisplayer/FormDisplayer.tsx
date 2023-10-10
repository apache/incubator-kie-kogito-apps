import React, {
  useEffect,
  useImperativeHandle,
  useMemo,
  useState
} from 'react';
import { Bullseye } from '@patternfly/react-core/dist/js/layouts/Bullseye';
import {
  componentOuiaProps,
  OUIAProps
} from '@kogito-apps/ouia-tools/dist/utils/OuiaUtils';
import { BallBeat } from 'react-pure-loaders';
import { Form, FormOpened, FormOpenedState } from '../../../api';
import ReactFormRenderer from '../ReactFormRenderer/ReactFormRenderer';
import HtmlFormRenderer from '../HtmlFormRenderer/HtmlFormRenderer';
import '../styles.css';
import {
  FormConfig,
  EmbeddedFormApi,
  InternalFormDisplayerApi,
  InternalFormDisplayerApiImpl
} from './apis';

interface FormDisplayerProps {
  isEnvelopeConnectedToChannel: boolean;
  content: Form;
  data: any;
  onOpenForm: (opened: FormOpened) => void;
  context: Record<string, string>;
}

export const FormDisplayer = React.forwardRef<
  EmbeddedFormApi,
  FormDisplayerProps & OUIAProps
>(
  (
    {
      isEnvelopeConnectedToChannel,
      content,
      data,
      context,
      onOpenForm,
      ouiaId,
      ouiaSafe
    },
    forwardedRef
  ) => {
    const [source, setSource] = useState<string>();
    const [resources, setResources] = useState<any>();
    const [formData, setFormData] = useState<string>();
    const [formApi, setFormApi] = useState<InternalFormDisplayerApi>(null);
    const [isExecuting, setIsExecuting] = useState<boolean>(false);

    const doOpenForm = (config: FormConfig): EmbeddedFormApi => {
      const api: EmbeddedFormApi = {};
      setFormApi(new InternalFormDisplayerApiImpl(api, config.onOpen));
      return api;
    };

    const canDisplayForm = useMemo(() => {
      return isEnvelopeConnectedToChannel && !isExecuting && source;
    }, [isEnvelopeConnectedToChannel, isExecuting, source]);

    useEffect(() => {
      window.Form = {
        openForm: doOpenForm
      };
    }, []);

    useEffect(() => {
      /* istanbul ignore else */
      if (isEnvelopeConnectedToChannel) {
        setSource(content.source);
        setResources(content.configuration.resources);
        setFormData(data);
      }
    }, [isEnvelopeConnectedToChannel, content, data]);

    useEffect(() => {
      if (isEnvelopeConnectedToChannel && formApi) {
        formApi.onOpen({
          data: formData,
          context: context
        });
        setTimeout(() => {
          onOpenForm({
            state: FormOpenedState.OPENED,
            size: {
              height: document.body.scrollHeight,
              width: document.body.scrollWidth
            }
          });
        }, 500);
      }
    }, [formApi]);

    useImperativeHandle(forwardedRef, () => formApi, [formApi]);

    return (
      <div {...componentOuiaProps(ouiaId, 'form-displayer', ouiaSafe)}>
        {canDisplayForm ? (
          <div id={'inner-form-container'}>
            {content.formInfo && content.formInfo.type === 'TSX' ? (
              <ReactFormRenderer
                source={source}
                resources={resources}
                setIsExecuting={setIsExecuting}
              />
            ) : (
              <HtmlFormRenderer source={source} resources={resources} />
            )}
          </div>
        ) : (
          <Bullseye className="kogito-form-displayer__ball-beats">
            <BallBeat
              color={'#000000'}
              loading={!isEnvelopeConnectedToChannel}
            />
          </Bullseye>
        )}
      </div>
    );
  }
);

export default FormDisplayer;
