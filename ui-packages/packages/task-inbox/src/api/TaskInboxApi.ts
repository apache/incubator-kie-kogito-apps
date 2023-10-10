export interface TaskInboxApi {
  taskInbox__notify: (userName: string) => Promise<void>;
}
