var gulp = require('gulp');
var browserSync = require('browser-sync');
var url = require('url');
var proxy = require('proxy-middleware');

// proxying to api on host os for develop
var aa_proxy = url.parse('http://10.0.2.2:18080/auth-access/api');
aa_proxy.route = '/api/auth-access';
var tm_proxy = url.parse('http://10.0.2.2:28080/task-manage/api');
tm_proxy.route = '/api/task-manage';
var ws_proxy = url.parse('http://10.0.2.2:38080/widget-store/api');
ws_proxy.route = '/api/widget-store';

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
        proxy(aa_proxy),
        proxy(tm_proxy),
        proxy(ws_proxy),
        function (req, res, next) {
          res.setHeader('Access-Control-Allow-Origin', '*');
          next();
        }
      ]
    }
  }, done);
});
