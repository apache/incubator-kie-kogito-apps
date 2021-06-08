export interface RuntimeToolsDevUIEnvelopeApi {
  runtimeToolsDevUI_initRequest(association: Association, initArgs: argTypes);
}

export interface Association {
  origin: string;
  envelopeServerId: string;
}

export interface argTypes {
  users: string[];
  dataIndexUrl: string;
}
