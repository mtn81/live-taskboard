var gulp = require('gulp');
var browserSync = require('browser-sync');
var url = require('url');

var proxyMiddleware = require("http-proxy-middleware");

var aa_proxy = proxyMiddleware('/api/auth-access', {
  target: 'http://10.0.2.2:18080',
  pathRewrite: { '^/api/auth-access': '/auth-access/api' }
});
var tm_proxy = proxyMiddleware('/api/task-manage', {
  target: 'http://10.0.2.2:28080',
  pathRewrite: { '^/api/task-manage': '/task-manage/api' }
});
var ws_proxy = proxyMiddleware('/api/widget-store', {
  target: 'http://10.0.2.2:38080',
  pathRewrite: { '^/api/widget-store': '/widget-store/api' }
});

// this task utilizes the browsersync plugin
// to create a dev server instance
// at http://localhost:9000
gulp.task('serve', ['build'], function(done) {
  browserSync({
    open: false,
    port: 9000,
    server: {
      baseDir: ['.'],
      middleware: [
        aa_proxy,
        tm_proxy,
        ws_proxy,
        function (req, res, next) {
          res.setHeader('Access-Control-Allow-Origin', '*');
          next();
        }
      ]
    }
  }, done);
});
