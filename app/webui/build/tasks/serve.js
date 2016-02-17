var gulp = require('gulp');
var browserSync = require('browser-sync');
var url = require('url');

var proxyMiddleware = require("http-proxy-middleware");

var aa_proxy = proxyMiddleware('http://10.0.2.2:18080/auth-access/api');
var tm_proxy = proxyMiddleware('http://10.0.2.2:28080/task-manage/api');
var ws_proxy = proxyMiddleware('http://10.0.2.2:38080/widget-store/api');

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
