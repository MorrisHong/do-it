module.exports = {
  lintOnSave: false,
  outputDir: '../src/main/resources/static/',
  indexPath: '../templates/index.html',
  devServer: {
    port: 3000,
    proxy: {
      '/api/*': {
        target: 'http://localhost:8080'
      }
    }
  },
  configureWebpack: {
    entry: {
      app: './src/main.js',
      style: [
        'bootstrap/dist/css/bootstrap.css'
      ]
    }
  }
}
