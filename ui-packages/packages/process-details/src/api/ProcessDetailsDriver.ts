import {
  ProcessInstance,
  Job,
  JobCancel,
  SvgSuccessResponse,
  SvgErrorResponse,
  TriggerableNode,
  NodeInstance
} from '@kogito-apps/management-console-shared/dist/types';
export interface ProcessDetailsDriver {
  getProcessDiagram(
    data: ProcessInstance
  ): Promise<SvgSuccessResponse | SvgErrorResponse>;
  handleProcessAbort(processInstance: ProcessInstance): Promise<void>;
  cancelJob(job: Pick<Job, 'id' | 'endpoint'>): Promise<JobCancel>;
  rescheduleJob(
    job,
    repeatInterval: number | string,
    repeatLimit: number | string,
    scheduleDate: Date
  ): Promise<{ modalTitle: string; modalContent: string }>;
  getTriggerableNodes(
    processInstance: ProcessInstance
  ): Promise<TriggerableNode[]>;
  handleNodeTrigger(
    processInstance: ProcessInstance,
    node: TriggerableNode
  ): Promise<void>;
  handleProcessVariableUpdate(
    processInstance: ProcessInstance,
    updatedJson: Record<string, unknown>
  );
  processDetailsQuery(id: string): Promise<ProcessInstance>;
  jobsQuery(id: string): Promise<Job[]>;
  openProcessInstanceDetails(id: string): void;
  handleProcessRetry(processInstance: ProcessInstance): Promise<void>;
  handleNodeInstanceCancel(
    processInstance: ProcessInstance,
    node: NodeInstance
  ): Promise<void>;
  handleProcessSkip(processInstance: ProcessInstance): Promise<void>;
  handleNodeInstanceRetrigger(
    processInstance: ProcessInstance,
    node: Pick<NodeInstance, 'id'>
  ): Promise<void>;
}
