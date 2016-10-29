define([
    'jquery',
    'angular',
    'moment',
    'bootstrap',
    'angular-route',
    'angular-ui-router',
    'angular-resource',
    'angular-sanitize',
    'angular-spinner',
    'angular-animate',
    'angular-material',
    'js/controllers',
    'directives',
    'js/modules/httpinterceptor',
    'js/modules/query'
], function ($, angular, moment) {
    var app = angular.module('app', [
        'angularSpinner',
        'ui.router',
        'ngRoute',
        'ngResource',
        'ngSanitize',
        'ngAria',
        'ngMaterial',
        'controllers', 'directives', 'commons.interceptor', 'commons.http'
    ]).config(function ($httpProvider, $stateProvider, $urlRouterProvider, usSpinnerConfigProvider, $mdDateLocaleProvider) {
        $httpProvider.interceptors.push('HTTPInterceptor');
        usSpinnerConfigProvider.setDefaults({
            color: '#d9534f',
            length: 5,
            width: 3
        });
        $mdDateLocaleProvider.formatDate = function (date) {
            return (date) ? moment(date).format('DD/MM/YYYY') : '';
        };
        $mdDateLocaleProvider.parseDate = function (dateString) {
            var m = moment(dateString, 'DD/MM/YYYY', true);
            return m.isValid() ? m.toDate() : new Date(NaN);
        };
        // For any unmatched url, send to /route1
        if (userRole === 'admin') $urlRouterProvider.otherwise("/sheet");
        else  $urlRouterProvider.otherwise("/time")

        $stateProvider
            .state('time', {
                url: '/time',
                controller: "timeController",
                templateProvider: function ($stateParams, $templateFactory) {
                    return $templateFactory.fromUrl(contextPath + "/views/" + userRole + "/time.html", $stateParams);
                }
            })
            .state('sheet', {
                url: '/sheet',
                controller: "sheetController",
                templateProvider: function ($stateParams, $templateFactory) {
                    return $templateFactory.fromUrl(contextPath + "/views/" + userRole + "/sheet.html", $stateParams);
                }
            })
            .state('profile', {
                url: '/profile',
                controller: "usersController",
                templateProvider: function ($stateParams, $templateFactory) {
                    return $templateFactory.fromUrl(contextPath + "/views/" + userRole + "/profile.html", $stateParams);
                }
            })
            .state('project', {
                url: '/project',
                controller: "projectController",
                templateProvider: function ($stateParams, $templateFactory) {
                    return $templateFactory.fromUrl(contextPath + "/views/" + userRole + "/project.html", $stateParams);
                }
            });
    }).run(function ($rootScope, $location, $templateCache, util) {
        // util.buildCache();
        // register listener to watch route changes
        $rootScope.$on("$routeChangeStart", function (event, next, current) {
            //debugger;
            // http://stackoverflow.com/questions/6677035/jquery-scroll-to-element
            $('html, body').animate({
                scrollTop: 0
            }, 1150);
        });
    });
    return app;
});
