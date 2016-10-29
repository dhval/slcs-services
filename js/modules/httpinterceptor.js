define(['angular', 'angular-spinner', 'js/modules/utils'], function (angular) {
    angular.module('commons.interceptor', ['commons.utils', 'angularSpinner']).factory('HTTPInterceptor',
        ['$rootScope', '$q', '$timeout', '$log', 'props', 'utils', 'usSpinnerService', function ($rootScope, $q, $timeout, $log, props, utils, usSpinnerService) {

            var timeoutId = 0;

            function _start() {
                $("[ng-view]").css({
                    opacity: 0.65
                });
                usSpinnerService.spin('spinner-1');
            }

            function _stop() {
                $("[ng-view]").css({
                    opacity: 1
                });
                usSpinnerService.stop('spinner-1');
            }

            function showSpinner(show) {
                if (show) {
                    // $log.log("cancel previous timers: " + $timeout.cancel(timeoutId));
                    timeoutId = $timeout(function () {
                        _stop();
                    }, props.TIME_OUT);
                    _start();
                } else {
                    $timeout.cancel(timeoutId);
                    _stop();
                }
            }

            function responseErrorHandler() {
                var defError = {error: "Internal Server Error"};
                if (this.status === 403 || (this.status >= 300 && this.status < 400)) {
                    $rootScope.$broadcast('event:redirect');
                } else if (this.status == 400) {
                    var error = utils.getNestedProperty(this, "data.validate");
                    error = error || defError;
                    $rootScope.$broadcast('event:validate', error);
                } else {
                    var error = utils.getNestedProperty(this, "data.error");
                    error = error || defError;
                    $rootScope.$broadcast('event:error', error || defError);
                }
            }

            function isValidResponse(r) {
                if (r.status && r.status !== 200)
                    return false;
                if (r.headers('Request-Source') !== 'APPLICATION')
                    return false;
                var data = (r.data) ? r.data : r;
                if (!data || data.error)
                    return false;
                if (data['key'] && data['key'] === 'validate')
                    return false;
                if (data['content'] && data['title'])
                    return false;
                else
                    return true;
            }

            function parseErrorResponse(r) {
                if (r && r.data && r.data.key)
                    return r.data;
                if (r && r.data && r.data.error)
                    return r.data.error;
                else
                    return props.DEFAULT_ERROR;
            }

            return {
                'request': function (config) {
                    if (config['method'] === 'POST') {
                        var isSpinner = utils.getNestedProperty(config, "params.spinner");
                        showSpinner(isSpinner);
                    }
                    return config;
                },
                'requestError': function (rejection) {
                    return $q.reject(rejection);
                },
                'response': function (response) {
                    if (response['config']['method'] === 'POST') {
                        showSpinner(false);
                        if (!isValidResponse(response)) {
                            return $q.reject(parseErrorResponse(response));
                        }
                        return $q.resolve(response.data);
                    }
                    return response;
                },
                'responseError': function (rejection) {
                    showSpinner(false);
                    responseErrorHandler.call(rejection);
                    var isValidationError = utils.getNestedProperty(rejection, "data.key");
                    if (isValidationError) {
                        return $q.reject(rejection);
                    }
                    $log.log(JSON.stringify(rejection));
                    return $q.reject(props.DEFAULT_ERROR);
                }
            }
        }]);
});