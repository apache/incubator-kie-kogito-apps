module.exports = {
  setupFiles: [
    './config/Jest-config/test-shim.js',
    './config/Jest-config/test-setup.js',
    'core-js'
  ],
  moduleFileExtensions: ['ts', 'tsx', 'js'],
  coveragePathIgnorePatterns: [
    './src/static',
    'dist/',
    './src/envelope/index.ts',
    './src/embedded/tests/utils/Mocks.ts',
    './src/embedded/tests/mocks/Mocks.ts',
    './src/envelope/tests/mocks',
    './src/envelope/ProcessDetailsEnvelope.tsx'
  ],
  coverageReporters: [
    [
      'lcov',
      {
        projectRoot: '../../'
      }
    ]
  ],
  snapshotSerializers: ['enzyme-to-json/serializer'],
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
