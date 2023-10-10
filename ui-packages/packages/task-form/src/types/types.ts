export enum FormType {
  HTML = 'HTML',
  TSX = 'TSX'
}

export type FormInfo = {
  type: FormType;
  name: string;
  lastModified: Date;
};

export type FormConfiguration = {
  resources: FormResources;
  schema: string;
};

export type FormResources = {
  scripts: Record<string, string>;
  styles: Record<string, string>;
};

export type CustomForm = {
  formInfo: FormInfo;
  source: string;
  configuration: FormConfiguration;
};
