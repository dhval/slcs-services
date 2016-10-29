define(['angular'], function (angular) {
   angular.module('commons.utils', []).factory('utils', ['$q', '$timeout', function ($q, $timeout) {
       return {
           getNestedProperty : function (obj, key) {
                var keys = key.split('.');
                return keys.reduce(function (o, p) {
                    return typeof o == "undefined" || o === null ? o : o[p];
                }, obj);
           },
           debounce: function (id, timeout) {
               var timeoutId = 0;
               return function () {
                   var deferred = $q.defer();
                   if (timeoutId !== 0) {
                       setTimeout(function () {
                           deferred.reject();
                       }, 10);
                   } else {
                       timeoutId = $timeout(function () {
                           timeoutId = 0;
                       }, timeout);
                       deferred.resolve();
                   }
                   return deferred.promise;
               };
           }
       }
   }]);
});