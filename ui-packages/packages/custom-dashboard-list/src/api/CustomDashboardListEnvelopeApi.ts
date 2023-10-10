/**
 * Envelope Api
 */
export interface CustomDashboardListEnvelopeApi {
  /**
   * Initializes the envelope.
   * @param association
   */
  customDashboardList__init(association: Association): Promise<void>;
}

export interface Association {
  origin: string;
  envelopeServerId: string;
}

export interface CustomDashboardFilter {
  customDashboardNames: string[];
}

export interface CustomDashboardInfo {
  name: string;
  path: string;
  lastModified: Date;
}
