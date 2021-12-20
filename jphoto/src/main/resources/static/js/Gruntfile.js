module.exports = function (grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        autoprefixer: {
            options: {
                browsers: ['last 2 version', 'ie 9']
            },
            dev: {
                src: 'src/css/*.css',
                dest: 'dev/css/',
                flatten: true,
                expand: true
            }
        },
        copy: {
            dev: {
                cwd: 'src',
                src: ['*.html', 'js/**/*.js'],
                dest: 'dev/',
                expand: true
            }
        },
        clean: {
            dev: ['dev']
        },
        watch: {
            dev: {
                files: ['src/**/*.*'],
                tasks: ['autoprefixer:dev', 'copy:dev'],
                options: {
                    livereload: 35729
                }
            }
        }
    });

    grunt.loadNpmTasks('grunt-autoprefixer');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-watch');

    grunt.registerTask('default', ['autoprefixer:dev', 'copy:dev']);
}
