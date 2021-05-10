
const path = require('path');
const TsconfigPathsPlugin = require('tsconfig-paths-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");
const nodeModulesDir = "../.." + (__dirname.includes("node_modules") ? "" : "/node_modules");
module.exports = {
  entry: {
    app: path.resolve(__dirname, 'src', 'consoles/index.ts')
  },
  plugins: [new CopyPlugin({ patterns: [{ from: "./resources", to: "./resources" },
  {from: path.resolve(__dirname, nodeModulesDir + "/@kogito-apps/runtime-tools-dev-ui-webapp/dist"), to: './webapp'}
] })],
  module: {
    rules: [
      {
        test: /\.(tsx|ts)?$/,
        include: [
          path.resolve(__dirname, 'src')
        ],
        use: [
          {
            loader: 'ts-loader',
            options: {
              configFile: path.resolve('./tsconfig.json'),
              allowTsInNodeModules: true
            }
          }
        ]
      },
    ]
  },
  output: {
    filename: '[name].bundle.js',
    path: path.resolve(__dirname, 'dist'),
    publicPath: '/'
  },
  resolve: {
    extensions: ['.ts', '.tsx', '.js'],
    modules: [
      path.resolve('../../node_modules'),
      path.resolve('./node_modules'),
      path.resolve('./src')
    ],
    plugins: [
      new TsconfigPathsPlugin({
        configFile: path.resolve(__dirname, './tsconfig.json')
      })
    ],
    symlinks: false,
    cacheWithContext: false
  }
};