/**
 * Envelope Api
 */
export interface TaskInboxEnvelopeApi {
  /**
   * Initializes the envelope.
   * @param association
   * @param initArgs
   */
  taskInbox__init(
    association: Association,
    initArgs: TaskInboxInitArgs
  ): Promise<void>;
  taskInbox__notify(userName: string): Promise<void>;
}

export interface Association {
  origin: string;
  envelopeServerId: string;
}

export interface TaskInboxInitArgs {
  initialState?: TaskInboxState;
  allTaskStates?: string[];
  activeTaskStates?: string[];
}

/**
 * Representation of the TaskInbox state containing information about the applied filters, sorting and the current page.
 * This state will be shared between the channel and the TaskInbox.
 */
export interface TaskInboxState {
  filters: QueryFilter;
  sortBy: SortBy;
  currentPage: QueryPage;
}

/**
 * Filter applied in TaskInbox.
 */
export interface QueryFilter {
  taskStates: string[];
  taskNames: string[];
}

/**
 * Sorting applied in TaskInbox
 */
export interface SortBy {
  property: string;
  direction: 'asc' | 'desc';
}

/**
 * The last page of elements loaded in TaskInbox
 */
export interface QueryPage {
  offset: number;
  limit: number;
}
