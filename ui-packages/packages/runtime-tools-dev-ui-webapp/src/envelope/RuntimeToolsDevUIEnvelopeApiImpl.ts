import { EnvelopeApiFactoryArgs } from '@kie-tools-core/envelope';
import {
  Association,
  RuntimeToolsDevUIChannelApi,
  RuntimeToolsDevUIEnvelopeApi,
  RuntimeToolsDevUIInitArgs
} from '../api';
import { RuntimeToolsDevUIEnvelopeContextType } from './RuntimeToolsDevUIEnvelopeContext';
import { RuntimeToolsDevUIEnvelopeViewApi } from './RuntimeToolsDevUIEnvelopeViewApi';

export class RuntimeToolsDevUIEnvelopeApiImpl
  implements RuntimeToolsDevUIEnvelopeApi
{
  private view: () => RuntimeToolsDevUIEnvelopeViewApi;
  private capturedInitRequestYet = false;

  constructor(
    private readonly args: EnvelopeApiFactoryArgs<
      RuntimeToolsDevUIEnvelopeApi,
      RuntimeToolsDevUIChannelApi,
      RuntimeToolsDevUIEnvelopeViewApi,
      RuntimeToolsDevUIEnvelopeContextType
    >
  ) {}

  private hasCapturedInitRequestYet() {
    return this.capturedInitRequestYet;
  }

  private ackCapturedInitRequest() {
    this.capturedInitRequestYet = true;
  }

  public runtimeToolsDevUI_initRequest = async (
    association: Association,
    initArgs: RuntimeToolsDevUIInitArgs
  ): Promise<void> => {
    if (this.hasCapturedInitRequestYet()) {
      return;
    }
    this.args.envelopeClient.associate(
      association.origin,
      association.envelopeServerId
    );

    this.ackCapturedInitRequest();
    this.view = await this.args.viewDelegate();
    this.view().setDataIndexUrl(initArgs.dataIndexUrl);
    this.view().setTrustyServiceUrl(initArgs.trustyServiceUrl);
    this.view().setUsers(initArgs.users);
    this.view().navigateTo(initArgs.page);
    this.view().setDevUIUrl && this.view().setDevUIUrl(initArgs.devUIUrl);
    this.view().setOpenApiPath &&
      this.view().setOpenApiPath(initArgs.openApiPath);
    this.view().setAvailablePages &&
      this.view().setAvailablePages(initArgs.availablePages);
    this.view().setCustomLabels &&
      this.view().setCustomLabels(initArgs.customLabels);
    this.view().setOmittedProcessTimelineEvents &&
      this.view().setOmittedProcessTimelineEvents(
        initArgs.omittedProcessTimelineEvents
      );
    this.view().setDiagramPreviewSize &&
      this.view().setDiagramPreviewSize(initArgs.diagramPreviewSize);
    this.view().setIsStunnerEnabled &&
      this.view().setIsStunnerEnabled(initArgs.isStunnerEnabled);
    // Ensure these are set last. This is a workaround to ensure views are corrected configured with other properties
    // from the DevUIAppContext before they are rendered. i.e. use of DevUIAppContext is not responsive to updates.
    this.view().setProcessEnabled(initArgs.isDataIndexAvailable);
    this.view().setTracingEnabled(initArgs.isTracingEnabled);
  };
}
