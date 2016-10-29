define([
    'jquery',
    'angular',
    'moment',
    'bootstrap',
    'angular-route',
    'angular-resource',
    'angular-touch',
    'angular-sanitize',
    'angular-spinner',
    'angular-animate',
    'angular-material',
    'js/controllers',
    'directives'
], function($, angular, moment) {
    var app = angular.module('app', [
        'angularSpinner',
        'ngRoute',
        'ngResource',
        'ngSanitize',
        'ngTouch',
        'ngAria',
        'ngMaterial',
        'controllers',
        'directives'
    ]).config(function($locationProvider, $routeProvider, usSpinnerConfigProvider, $mdDateLocaleProvider) {
        usSpinnerConfigProvider.setDefaults({
            color: '#d9534f',
            length: 5,
            width: 3
        });
        $mdDateLocaleProvider.formatDate = function(date) {
            return moment(date).format('YYYY-MM-DD');
        };
        $mdDateLocaleProvider.parseDate = function(dateString) {
            var m = moment(dateString, 'YYYY-MM-DD', true);
            return m.isValid() ? m.toDate() : new Date(NaN);
        };
        $routeProvider
            .when('/', {
                templateUrl: contextPath + "/views/form.html",
                controller: "formController"
            })
            .when('/time', {
                templateUrl: contextPath + "/views/time.html",
                controller: "sheetController"
            })
            .when('/sheet', {
                templateUrl: contextPath + "/views/sheet.html",
                controller: "sheetController"
            })
            .when('/users', {
                templateUrl: contextPath + "/views/users.html",
                controller: "usersController"
            })
            .otherwise({
                redirectTo: '/'
            });
    }).run(function($rootScope, $location, $templateCache, util) {
        util.buildCache();
        // register listener to watch route changes
        $rootScope.$on("$routeChangeStart", function(event, next, current) {
            //debugger;
            // http://stackoverflow.com/questions/6677035/jquery-scroll-to-element
            $('html, body').animate({
                scrollTop: 0
            }, 1150);
        });
    });
    return app;
});
