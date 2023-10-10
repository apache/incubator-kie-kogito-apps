export interface JobsManagementEnvelopeApi {
  jobsManagement__init(association: Association);
}
export interface Association {
  origin: string;
  envelopeServerId: string;
}

export interface QueryPage {
  offset: number;
  limit: number;
}
