export enum ProcessInstanceState {
  Pending = 'PENDING',
  Active = 'ACTIVE',
  Completed = 'COMPLETED',
  Aborted = 'ABORTED',
  Suspended = 'SUSPENDED',
  Error = 'ERROR'
}

export enum MilestoneStatus {
  Available = 'AVAILABLE',
  Active = 'ACTIVE',
  Completed = 'COMPLETED'
}

export type NodeInstance = {
  __typename?: 'NodeInstance';
  id: string;
  name: string;
  type: string;
  enter: Date;
  exit?: Date;
  definitionId: string;
  nodeId: string;
};

export type Milestone = {
  __typename?: 'Milestone';
  id: string;
  name: string;
  status: MilestoneStatus;
};

export type ProcessInstanceError = {
  __typename?: 'ProcessInstanceError';
  nodeDefinitionId: string;
  message?: string;
};

export type ProcessInstance = {
  id: string;
  processId: string;
  processName?: string;
  parentProcessInstanceId?: string;
  rootProcessInstanceId?: string;
  rootProcessId?: string;
  roles?: string[];
  state: ProcessInstanceState;
  endpoint: string;
  serviceUrl?: string;
  nodes: NodeInstance[];
  milestones?: Milestone[];
  variables?: string;
  start: Date;
  end?: Date;
  parentProcessInstance?: ProcessInstance;
  childProcessInstances?: ProcessInstance[];
  error?: ProcessInstanceError;
  addons?: string[];
  lastUpdate: Date;
  businessKey?: string;
};
