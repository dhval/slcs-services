/**
 * Created by dhval on 7/20/15.
 */
define(['angular', 'moment', 'js/modules/query'], function(ng, moment) {
    var module = ng.module('services', ['ngResource', 'commons.http']);

    module.service('util', ['$log', '$timeout', '$templateCache', '$q', '$http', 'usSpinnerService', 'props',
        function($log, $timeout, $templateCache, $q, $http, usSpinnerService, props) {
            function getTemplate(type) {
                switch (type) {
                    case 'CLEAN':
                        return $templateCache.get("view.clean.html");
                    case 'AOPC':
                        return $templateCache.get("view.aopc.html");
                    case 'DRO':
                        return $templateCache.get("view.dro.html");
                }
            }

            function buildCache() { }

            function buildCache2() {
                var ctxPath = props.ctxPath;
                $templateCache.put('view.clean.html',
                    "<tr><td colspan='7'><div class='panel'> <div class='panel-header transaction-header'><h5>{{currentDataSource.replace('_', ' (') + ') Summary - As of ' + data.queryTime}}</h5>" +
                    "<p><strong>Note:</strong> This document is for informational purposes only and must not be used for commitment purposes.</p>" +
                    "</div><div class='panel-body'><pre class='text'>{{data.cleanTxt}}</pre></div></div></td></tr>");

                function push(templateUrl, templateId) {
                    $http.get(ctxPath + templateUrl, {
                        cache: $templateCache
                    }).success(function(data) {
                        $templateCache.put(templateId, data);
                    });
                }

                push('/views/template_aopc.html', 'view.aopc.html')
                push('/views/template_dro.html', 'view.dro.html')

                push('/views/datepicker.html', 'template/datepicker/datepicker.html')
                push('/views/popup.html', 'template/datepicker/popup.html')
                push('/views/day.html', 'template/datepicker/day.html')
                push('/views/month.html', 'template/datepicker/month.html')
                push('/views/year.html', 'template/datepicker/year.html')
            }

            function pop(url, target) {
                var win = window.open(url, target);
                if (win) {
                    win.focus();
                } else {
                    alert('Please allow us to open pop-up window.');
                }
            }

            function parseCriteria(search) {
                var criteria = {};
                if (!search)
                    return criteria;
                if (search.name)
                    criteria['Name'] = search.name;
                if (search.fieldSSN)
                    criteria['SSN'] = search.fieldSSN;
                if (search.birthDt)
                    criteria['DOB'] = search.birthDt;
                if (search.sex)
                    criteria['SEX'] = search.sex;
                if (search.fieldSID)
                    criteria['SID'] = search.fieldSID;
                return criteria;
            }

            function clear(criteria) {
                ng.extend(criteria, {
                    lastName: '',
                    firstName: '',
                    sex: '',
                    fieldSSN: '',
                    fieldSID: '',
                    birthDt: ''
                });
            }

            // only one global spinner is allowed.
            function spinner() {
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
                return function(show) {
                    if (show) {
                        // $log.log("cancel previous timers: " + $timeout.cancel(timeoutId));
                        timeoutId = $timeout(function() {
                            _stop();
                        }, props.TIME_OUT);
                        _start();
                    } else {
                        $timeout.cancel(timeoutId);
                        _stop();
                    }
                }
            }

            function debounce(id, timeout) {
                var timeoutId = 0;
                return function() {
                    var deferred = $q.defer();
                    if (timeoutId !== 0) {
                        setTimeout(function() {
                            deferred.reject();
                        }, 10);
                    } else {
                        timeoutId = $timeout(function() {
                            timeoutId = 0;
                        }, timeout);
                        deferred.resolve();
                    }
                    return deferred.promise;
                };
            }

            function trim(str) {
                return str.replace(/^\s+|\s+$/g, "").replace(/\s+/g, " ").replace(
                        /(< ?br ?\/?>)*/g, "").replace(/["']/g, "").replace(
                        /(< ?\/?span ?\/?>)*/g, "").replace(/(< ?\/?strong ?\/?>)*/g, "")
                    .replace(/[\r\n]/g, " ");
            }

            return {
                trimHTML: trim,
                debounce: debounce,
                spin: spinner(),
                clear: clear,
                parseCriteria: parseCriteria,
                buildCache: buildCache,
                getTemplate: getTemplate,
                parseDate: function (dateString) {
                    var m = moment(dateString, 'DD/MM/YYYY', true);
                    return m.isValid() ? m.toDate() : new Date(NaN);
                },
                parseISODate: function (dateString) {
                    var m = moment(dateString.substring(0,10), 'YYYY-MM-DD', true);
                    return m.isValid() ? m.toDate() : new Date(NaN);
                },
                formatDate: function (date) {
                    return moment(date).format('DD/MM/YYYY');
                },
                formatDateTime: function (date) {
                    return moment(date).format('DD/MM/YYYY HH:mm');
                },
                calcEntry: function (days) {
                    return function (entry, k) {
                        entry['total'] = 0;
                        var num = Number.parseFloat(entry[k]);
                        if (!isNaN(num) || num > 24 || num <= 0) {
                            entry[k] = (Math.round(num * 4) / 4).toFixed(2);
                        } else {
                            entry[k] = 0;
                        }
                        days.forEach(function (key) {
                            entry['total'] += (entry[key]) ? Number.parseFloat(entry[key]) : 0;
                        })
                    }
                },
                clearSheet: function(sheet, days) {
                    sheet.status = 'entered';
                    sheet.entries.forEach(function(entry) {
                        days.forEach(function(day) {
                            entry[day] = 0;
                        });
                        entry['total'] = 0;
                    });
                },
                isPastSunday: function (date) {
                    var today = new Date();
                    // backoff 3 months
                    var past = new Date(today.getFullYear(), today.getMonth() - 3, 1);
                    return date.getDay() == 0 && date < today && date > past;
                }
            }
        }
    ]);

    module.service('query', ['$http', '_http', '$q', '$timeout', '$log', '$resource', '$templateCache', 'util', 'props',
        function($http, _http, $q, $timeout, $log, $resource, $templateCache, util, props) {

            var random = Math.floor((Math.random() * 10000) + 1);

            function isValid(r) {
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

            function parseError(r) {
                if (r && r.data)
                    return r.data;
                else
                    return props.DEFAULT_ERROR;
            }

            function handleSuccess(promise) {
                return function(r) {
                    util.spin(false);
                    promise.resolve(r);
                    // TODO
                    /**
                    if (r.data && !isValid(r)) {
                        var errorObj = parseError(r);
                        promise.reject(errorObj);
                    } else {
                        promise.resolve(r.data);
                    }
                     */
                }
            }

            function handleError(promise) {
                return function(r) {
                    util.spin(false);
                    var errorObj = parseError(r);
                    promise.reject(errorObj);
                };
            }

            function handleUpload(uploadUrl, spin, token) {
                return function ($files, data) {
                    util.spin(spin);
                    var deferred = $q.defer();
                    var formData = new FormData();
                    angular.forEach($files, function (value, key) {
                        // file name must match the spring controller's field
                        formData.append("file", value);
                    });
                    formData.append('timesheet', new Blob([JSON.stringify(data)], {
                        type: "application/json"
                    }));
                    $http({
                        method: 'POST',
                        url: uploadUrl,
                        headers: {'Content-Type': undefined,  "X-CSRF-TOKEN": token},
                        data: formData
                    }).then(handleSuccess(deferred), handleError(deferred));
                    return deferred.promise;
                };
            }

            function handlePost(spin, token) {
                return function(pathAPI, data, params) {
                    util.spin(spin);
                    // we want to wrap within a new promise, as we may reject a valid response.
                    var deferred = $q.defer();
                    $http.post(props.API_PATH + pathAPI + '?count=' + (++random), data, {
                        params: params,
                        headers: {
                            "X-CSRF-TOKEN": token
                        }
                    }).then(handleSuccess(deferred), handleError(deferred));
                    return deferred.promise;
                };
            }

            function logout(){
                var handler = handlePost(true, props.token);
            }

            var httpPost = _http['handlePost'](props.API_PATH, props.token);
            var httpGet = _http['handleGet'](props.API_PATH, props.token)
            var httpPut = _http['handlePut'](props.API_PATH, props.token)
            return {
                isMock: false,
                handleUpload: handleUpload(props.API_PATH + '/sheet/upload', true, props.token),
                postJSON: handlePost(true, props.token),
                httpPut: httpPut,
                httpPost: httpPost,
                httpGet: httpGet,
                saveSheet: function (sheet) {
                    if (!sheet.status || sheet.status == 'created') sheet.status = 'entered';
                    return httpPut("/sheet", sheet);
                }
            };
        }
    ]);

    module.factory('aggregator', ['query', '$log', '$q', 'util', function(query, $log, $q, util) {
        var cache = {};
        var visted = {};
        var criteria = {};

        function getData() {
            return cache;
        }

        function getSummary(query) {
            var listName = 'hotQuery';
            if (query.match("(.)*_DQ")) {
                listName = 'driverQuery';
            }
            else if (query.match("(.)*_WARRANT")) {
                listName = 'warrantQuery';
            }
            return cache[listName][query].summaryList;
        }

        function getDetails(summary) {
            var deferred = $q.defer();
            if (visted[summary['adminId']])
                return visted[summary['adminId']];
            var criteria = cache['criteria'];
            ng.extend(criteria, {
                adminId: summary['adminId'],
                query: summary['query'],
                agency: summary['agency']
            });
            query.getDetails(criteria).then(function(data) {
                visted[summary['adminId']] = data;
                deferred.resolve(data);
            }, function(data) {
                deferred.reject(data);
            });
            return deferred.promise;
        }

        function isVisited(adminId) {
            if (adminId && visted[adminId])
                return true;
            else
                return false;
        }

        function getCriteria() {
            return criteria;
        }

        function getQueryId() {
            return cache['rsQueryId'];
        }

        function ingest(data) {
            data = (data.data) ? data.data : data;
            if (!cache['rsQueryId'] || cache['rsQueryId'] !== data['rsQueryId']) {
                cache = data;
                criteria = util.parseCriteria(data.criteria);
            } else {
                ng.merge(cache['driverQuery'], data['driverQuery']);
                ng.merge(cache['hotQuery'], data['hotQuery']);
                ng.merge(cache['warrantQuery'], data['warrantQuery'])
            }
        };

        return {
            getData: getData,
            getSummary: getSummary,
            getDetails: getDetails,
            getCriteria: getCriteria,
            isVisited: isVisited,
            getQueryId: getQueryId,
            ingest: ingest
        };
    }]);

});
