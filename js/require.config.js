require.config({
    "paths": {
        "angular": ["https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.5.8/angular.min",
            "https://s3.amazonaws.com/dhvalm/dist/angular/angular"],
        "angular-aria": ["https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.5.8/angular-aria.min",
            "https://s3.amazonaws.com/dhvalm/dist/angular-aria/angular-aria"],
        "angular-material": ["https://cdnjs.cloudflare.com/ajax/libs/angular-material/1.1.1/angular-material.min",
            "https://s3.amazonaws.com/dhvalm/dist/angular-material/angular-material"],
        "angular-animate": ["https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.5.8/angular-animate.min",
            "https://s3.amazonaws.com/dhvalm/dist/angular-animate/angular-animate"],
        "angular-mocks": ["https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.5.8/angular-mocks",
            "https://s3.amazonaws.com/dhvalm/dist/angular-mocks/angular-mocks"],
        "angular-route": ["https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.5.8/angular-route.min",
            "https://s3.amazonaws.com/dhvalm/dist/angular-route/angular-route"],
        "angular-resource": ["https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.5.8/angular-resource.min",
            "https://s3.amazonaws.com/dhvalm/dist/angular-resource/angular-resource"],
        "angular-scenario": ["https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.5.8/angular-scenario",
            "https://s3.amazonaws.com/dhvalm/dist/angular-scenario/angular-scenario"],
        "angular-sanitize": ["https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.5.8/angular-sanitize.min",
            "https://s3.amazonaws.com/dhvalm/dist/angular-sanitize/angular-sanitize"],
        "angular-spinner": "https://s3.amazonaws.com/dhvalm/dist/angular-spinner/angular-spinner",
        "angular-ui-router": "https://s3.amazonaws.com/dhvalm/dist/angular-ui-router/angular-ui-router",
        "moment": "https://s3.amazonaws.com/dhvalm/dist/moment/moment",
        "spin": "https://s3.amazonaws.com/dhvalm/dist/spin.js/spin",
        "bootstrap": "https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min",
        "jquery": ["https://cdnjs.cloudflare.com/ajax/libs/jquery/1.12.4/jquery.min",
            "https://s3.amazonaws.com/dhvalm/dist/jquery/jquery"],
        "directives": "js/directives"
    },
    "shim": {
        "angular-route": {
            "deps": ['angular'],
            "exports": 'angular'
        },
        "angular-ui-router": {
            "deps": ['angular'],
            "exports": 'angular'
        },
        "angular-resource": {
            "deps": ['angular'],
            "exports": 'angular'
        },
        "angular-aria": {
            "deps": ['angular'],
            "exports": 'angular'
        },
        "angular-material": {
            "deps": ['angular', 'angular-aria'],
            "exports": 'angular'
        },
        "angular-animate": {
            "deps": ['angular'],
            "exports": 'angular'
        },
        "angular-sanitize": {
            "deps": ['angular'],
            "exports": 'angular'
        },
        "angular-spinner": {
            "deps": ['angular', 'spin'],
            "exports": 'angular'
        },
        "angular": {
            "deps": ['jquery', 'moment'],
            "exports": 'angular'
        },
        "directives": {
            "deps": ['angular'],
            "exports": 'angular'
        },
        "bootstrap": {
            "deps": ["jquery"]
        }
    }
});

// second require.config call needed because r.js processes only the first require.config call,
// and it will die if it sees an undeclared var like 'contextPath'.
require.config({
    "baseUrl": "./"
});
