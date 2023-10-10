module.exports = {
  preset: 'ts-jest/presets/js-with-ts',
  setupFiles: [
    './config/Jest-config/test-shim.js',
    './config/Jest-config/test-setup.js',
    'core-js'
  ],
  coveragePathIgnorePatterns: [
    './src/static',
    './tests/mocks/',
    './src/components/contexts',
    './src/components/pages/index.ts',
    './src/channel/apis/index.ts',
    './src/channel/CloudEventForm/',
    './src/channel/CustomDashboardList/index.ts',
    './src/channel/CustomDashboardView/index.ts',
    './src/channel/FormDetails/index.ts',
    './src/channel/FormsList/index.ts',
    './src/channel/JobsManagement/index.ts',
    './src/channel/ProcessDefinitionList/index.ts',
    './src/channel/ProcessDetails/index.ts',
    './src/channel/ProcessForm/index.ts',
    './src/channel/ProcessList/index.ts',
    './src/channel/TaskForms/index.ts',
    './src/channel/TaskInbox/index.ts',
    './src/channel/WorkflowForm/index.ts'
  ],
  coverageReporters: [
    [
      'lcov',
      {
        projectRoot: '../../'
      }
    ]
  ],
  moduleFileExtensions: ['ts', 'tsx', 'js'],
  globals: {
    'ts-jest': {
      isolatedModules: true
    }
  },
  transformIgnorePatterns: [],
  transform: {
    '^.+.jsx?$': './config/Jest-config/babel-jest-wrapper.js',
    '^.+.(ts|tsx)$': 'ts-jest',
    '.(jpg|jpeg|png|svg)$': './config/Jest-config/fileMocks.js'
  },
  testMatch: ['**/tests/*.(ts|tsx)'],
  moduleNameMapper: {
    '\\.(scss|sass|css)$': 'identity-obj-proxy',
    'monaco-editor': '<rootDir>/__mocks__/monacoMock.js'
  }
};
