module.exports = {
  setupFiles: [
    './config/Jest-config/test-shim.js',
    './config/Jest-config/test-setup.js',
    './config/Jest-config/canvas-mock.js',
    'core-js'
  ],
  moduleFileExtensions: ['ts', 'tsx', 'js'],
  coveragePathIgnorePatterns: [
    './src/static',
    'dist/',
    './src/envelope/WorkflowFormEnvelope.tsx',
    './src/envelope/tests/mocks/',
    './src/embedded/tests/mocks/'
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
    '.(scss|sass|css)$': 'identity-obj-proxy'
  }
};
