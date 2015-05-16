var gulp = require('gulp');
var browserSync = require('browser-sync');
var url = require('url');
var proxy = require('proxy-middleware');

// proxying to api on host os for develop
var proxyOptions = url.parse('http://10.0.2.2:8080/auth-access/api');
proxyOptions.route = '/api/auth-access';

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
        proxy(proxyOptions),
        function (req, res, next) {
          res.setHeader('Access-Control-Allow-Origin', '*');
          next();
        }
      ]
    }
  }, done);
});
