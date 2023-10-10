/**
 * Envelope Api
 */
export interface CustomDashboardViewEnvelopeApi {
  /**
   * Initializes the envelope.
   * @param association
   */
  customDashboardView__init(
    association: Association,
    dashboardName: string
  ): Promise<void>;
}

export interface Association {
  origin: string;
  envelopeServerId: string;
}
