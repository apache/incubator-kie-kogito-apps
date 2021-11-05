const CopyPlugin = require('copy-webpack-plugin');
const webpack = require('webpack');

const path = require('path');

const HOST = process.env.HOST || 'localhost';
const PORT = process.env.PORT || '9000';

module.exports = {
  mode: 'development',
  entry: {
    app: path.resolve(__dirname, './index.tsx')
  },
  output: {
    path: path.resolve('../dist-dev'),
    filename: '[name].js',
    publicPath: '/'
  },
  plugins: [
    new CopyPlugin({
      patterns: [
        { from: path.resolve(__dirname, '../static/images'), to: './images' },
        { from: path.resolve(__dirname, '../static/fonts'), to: './fonts' }
      ]
    }),
    new webpack.EnvironmentPlugin({
      KOGITO_APP_VERSION: 'DEV',
      KOGITO_APP_NAME: 'Trusty',
      KOGITO_TRUSTY_API_HTTP_URL: 'http://localhost:1336'
    })
  ],
  devtool: 'source-map',
  devServer: {
    contentBase: path.join(__dirname),
    host: HOST,
    port: PORT,
    compress: true,
    inline: true,
    historyApiFallback: true,
    hot: true,
    overlay: true,
    open: true
  },
  module: {
    rules: [
      {
        test: /\.(tsx|ts)?$/,
        use: [
          {
            loader: 'ts-loader',
            options: {
              configFile: path.resolve(__dirname, 'tsconfig.json'),
              allowTsInNodeModules: true,
              onlyCompileBundledFiles: true
            }
          }
        ]
      },
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader']
      },
      {
        test: /\.s[ac]ss$/i,
        use: ['style-loader', 'css-loader', 'sass-loader']
      },
      {
        test: /\.(svg|ttf|eot|woff|woff2)$/,
        include: [/fonts|pficon/],
        use: {
          loader: 'url-loader',
          options: {
            // Limit at 50k. larger files emitted into separate files
            limit: 5000,
            outputPath: 'fonts',
            name: '[name].[ext]'
          }
        }
      },
      {
        test: /\.svg$/,
        use: [
          {
            loader: 'url-loader',
            options: {
              limit: 5000,
              outputPath: 'svgs',
              name: '[name].[ext]'
            }
          }
        ]
      },
      {
        test: /\.(jpg|jpeg|png|gif)$/i,
        use: [
          {
            loader: 'url-loader',
            options: {
              limit: 5000,
              outputPath: 'images',
              name: '[name].[ext]'
            }
          }
        ]
      }
    ]
  },
  resolve: {
    extensions: ['.tsx', '.ts', '.js', '.jsx']
  }
};
