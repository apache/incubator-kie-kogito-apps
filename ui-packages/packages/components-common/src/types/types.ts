export enum SCHEMA_VERSION {
  DRAFT_7 = 'http://json-schema.org/draft-07/schema#',
  DRAFT_2019_09 = 'https://json-schema.org/draft/2019-09/schema'
}
export interface FormRendererApi {
  doReset: () => void;
}
