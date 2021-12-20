define(function (require, exports, module) {

    var Backbone = require('backbone');
    var _ = require('underscore');
    var json = jsonRPC;

    var JPhoto = function (options) {
        this.options = _.extend(this.defaults, options);
    };

    _.extend(JPhoto.prototype, {

        defaults: {
            baseUrl: '/'
        },

        url: function (path) {
            return this.options.baseUrl + '/' + path;
        },

        ajaxService: function () {
            return json.ajaxService;
        },

        imageFolder: function () {
            return this.options.imageFolder;
        },

        translate: function (nerd) {
            return this.ajaxService().translate(nerd);
        },

        translateAll: function (nerds) {
            return this.ajaxService().translateAll(nerds);
        }
    });

    Backbone.JPhoto = new JPhoto(module.config());
});
