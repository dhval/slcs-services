/* jshint node: true */
// ^ we need some node modules like path, require

module.exports = function(grunt) {

	var appConfig = {
		dist: 'dist'
	};

    grunt.initConfig({
        bower: {
            install: {
                options: {
                    verbose: true,
                    layout: 'byComponent',
                    targetDir: 'dist',
                    cleanTargetDir: true,
                    cleanBowerDir: false
                }
            }
        },
        clean: {
            dist: {
                src: [appConfig.dist]
            }
        },
        less: {
            vendor: {
                options: {
                    sourceMap: true,
                    compress: true,
                    yuicompress: true,
                    optimization: 2,
                    relativeUrls: true
                },
                files: {
                    'css/vendor.css': 'less/vendor.less'
                }
            },
            project: {
                options: {
                    sourceMap: true,
                    dumpLineNumbers: 'comments',
                    relativeUrls: true
                },
                files: {
                    'css/base.css': 'less/base.less'
                }
            }
        },
        concat: {
            options: {
                stripBanners: true
            },
            js: {
                options: {
                    separator: ';\n'
                },
                files: {
                    'js/directives.js': ["js/directives/directive.js", "js/directives/bingmap.js",
                        "js/directives/autofill.js", "js/directives/displaydetails.js",
                        "js/directives/formatpattern.js", "js/directives/inputmask.js",
                        "js/directives/panel.js", "js/directives/autotab.js", "js/directives/ngfile.js"
                    ]
                }
            }
        }
    });

    require('load-grunt-tasks')(grunt, {
        pattern: ['grunt-contrib-*']
    });

    grunt.loadNpmTasks('grunt-bower-task');

    grunt.registerTask('install', ['clean:dist', 'bower:install']);
}