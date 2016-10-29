define(['angular'], function (angular) {
    angular.module('commons.http', []).factory('_http', ['$http', function ($http) {
        return {
            handlePost: function(path, token) {
                return function (uri, data, spin, params) {
                    params = params || {};
                    angular.extend(params, {
                        'spinner' : spin
                    });
                    return $http.post(path + uri, data, {
                        params: params,
                        headers: {
                            "X-CSRF-TOKEN": token
                        }
                    });
                };
            },
            handlePut: function(path, token) {
                return function (uri, data, spin, params) {
                    params = params || {};
                    angular.extend(params, {
                        'spinner' : spin
                    });
                    return $http.put(path + uri, data, {
                        params: params,
                        headers: {
                            "X-CSRF-TOKEN": token
                        }
                    });
                };
            },
            handleGet : function(path, token) {
                return function (uri) {
                    return $http.get(path + uri, {
                        headers: {
                            'Content-type': 'application/json',
                            "X-CSRF-TOKEN": token
                        }
                    });
                }
            },
            handleUpload: function (uploadUrl, spin) {
                return function ($files, data) {
                    var formData = new FormData();
                    angular.forEach($files, function (value, key) {
                        // file name must match the spring controller's field
                        formData.append("file", value);
                    });
                    formData.append('watchlist', new Blob([JSON.stringify(data)], {
                        type: "application/json"
                    }));
                    return $http({
                        method: 'POST',
                        url: uploadUrl,
                        headers: {'Content-Type': undefined},
                        data: formData,
                        params: {
                            'spinner': spin
                        }
                    });
                };
            }
        }
    }]);
});