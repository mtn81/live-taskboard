var gulp = require('gulp');
var runSequence = require('run-sequence');
var del = require('del');
var vinylPaths = require('vinyl-paths');
var paths = require('../paths');
var bundles = require('../bundles.json');
var resources = require('../export.json');

// deletes all files in the output path
gulp.task('clean-export', function() {
  return gulp.src([paths.exportSrv])
    .pipe(vinylPaths(del));
});

function getBundles() {
  var bl = [];
  for (b in bundles.bundles) {
    bl.push(b + '.js');
    bl.push(b + '-*.js');
  }
  return bl;
}

function getExportList() {
  return resources.list.concat(getBundles());
}

gulp.task('export-copy', function() {
  gulp.src(getExportList(), {base: '.'})
    .pipe(gulp.dest(paths.exportSrv));
  gulp.src('src/**/*.css', {base: 'src'})
    .pipe(gulp.dest(paths.exportSrv));
});

// use after prepare-release
gulp.task('export', function(callback) {
  return runSequence(
    'bundle',
    'clean-export',
    'export-copy',
    callback
  );
});
