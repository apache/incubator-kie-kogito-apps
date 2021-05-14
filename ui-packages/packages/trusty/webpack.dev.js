const merge = require('webpack-merge');
const common = require('./webpack.common.js');
const webpack = require('webpack');

const HOST = process.env.HOST || 'localhost';
const PORT = process.env.PORT || '9000';

module.exports = env => {
  return merge(common, {
    mode: 'development',
    devtool: 'source-map',
    devServer: {
      contentBase: './dist',
      host: HOST,
      port: PORT,
      compress: true,
      inline: true,
      historyApiFallback: true,
      hot: true,
      overlay: true,
      open: true
    },
    plugins: [
      new webpack.EnvironmentPlugin({
        KOGITO_TRUSTY_COUNTERFACTUAL:
          env && env.includes('enableCF') ? 'enabled' : 'disabled'
      })
    ],
    module: {
      rules: [
        {
          test: /\.css$/,

          use: ['style-loader', 'css-loader']
        },
        {
          test: /\.s[ac]ss$/i,
          use: ['style-loader', 'css-loader', 'sass-loader']
        }
      ]
    },
    resolve: {
      extensions: ['.tsx', '.ts', '.js', '.jsx']
    }
  });
};
