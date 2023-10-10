module.exports = {
  setupFiles: [
    './config/Jest-config/test-shim.js',
    './config/Jest-config/test-setup.js',
    'core-js'
  ],
  moduleFileExtensions: ['ts', 'tsx', 'js'],
  coveragePathIgnorePatterns: [
    './src/static',
    './src/api/',
    'dist/',
    './src/envelope/WorkflowFormEnvelope.tsx',
    './src/embedded/tests/utils/Mocks.ts',
    './src/envelope/tests/mocks/Mocks.ts',
    './src/envelope/CloudEventFormEnvelope.tsx',
    './src/envelope/index.ts'
  ],
  coverageReporters: [
    [
      'lcov',
      {
        projectRoot: '../../'
      }
    ]
  ],
  transformIgnorePatterns: [],
  transform: {
    '^.+.jsx?$': './config/Jest-config/babel-jest-wrapper.js',
    '^.+.(ts|tsx)$': 'ts-jest',
    '.(jpg|jpeg|png|svg)$': './config/Jest-config/fileMocks.js'
  },
  testMatch: ['**/tests/*.(ts|tsx|js)'],
  moduleNameMapper: {
    '.(scss|sass|css)$': 'identity-obj-proxy',
    'monaco- editor': '<rootDir>/__mocks__/monacoMock.js'
  }
};
