define(['angular', 'jquery', 'js/services'], function (ng, $) {

    var module = angular.module('controllers', ['services', 'ngResource']);

    module.controller('appController', function ($scope, $rootScope, $mdDialog, $http, $timeout, $location, $window,
                                                 $log, props, util, query, aggregator) {
        var self = this;
        self.domainInfo = {};
        self.results = {};
        self.validation = [];
        self.error = {};
        self.userId = userName;
        self.metrics = {};

        function clearErrors() {
            // clear validation errors.
            self.validation = [];
            self.error = {};
        }

        function home() {
            window.location = window.location.pathname;
        }

        self.clear = function () {
            clearErrors();
            util.clear(self.domainInfo);
        };

        self.logout = function () {
            $http.post("/logout", {}, {
                headers: {
                    "X-CSRF-TOKEN": props.token
                }
            }).then(function (data) {
                $window.location.href = '';
            });
        };

        $rootScope.$on("event:validate", function(event, data) {
            self.validation.splice(0, self.validation.length);
            for(key in data)
                self.validation.push(data[key]);
        });

        $rootScope.$on("event:error", function(event, error) {
            self.error = error;
        });

        $rootScope.$on("event:redirect", home);

        // global error handler
        self.errorHandler = function (error) {
            self.validation = [];
            if (error['key'] && error['key'] === 'validate') {
                // validation errors
                var errors = error['value'];
                for (key in errors)
                    self.validation.push(errors[key]);
                if ($location.path() != '/')
                    $location.path('/');

            } else if (error['message']) {
                self.validation.push(error['message']);
                if ($location.path() != '/')
                    $location.path('/');
            } else if (error['content'] && error['title']) {
                // application errors
                self.error = error;
                if ($location.path() != '/')
                    $location.path('/');
            } else if (error) {
                self.validation.push(error);
                $location.path('/');
            } else {
                $window.location.href = '';
            }
        };

        // debounce (and reject) requests, if button pressed within 1 sec. timeout interval
        self.handleSubmit = (function (id, interval) {
            var debounce = util.debounce(id, interval);
            return function (criteria) {
                debounce().then(function () {
                    clearErrors();
                    query.postJSON(criteria).then(function (data) {
                        data = (data.status) ? data.data : data;
                        aggregator.ingest(data);
                        $location.path('/result');
                    }, self.errorHandler);
                }, function () {
                    $log.log("canceled an http request of type, " + id);
                });
            };
        })('query', 1000);


        self.addSheet = function () {
            $scope.$broadcast('addSheet')
        };

        self.mySheets = function () {
            $scope.$broadcast('mySheets')
        };

        self.isValid = function () {
            if ($("form.ng-invalid").length > 0) {
                $mdDialog.show($mdDialog.alert()
                    .title('Error')
                    .textContent('Fix errors on this page')
                    .ok('Okay')
                );
                return false;
            }
            return true;
        };

        self.showAlert = function (title, text, toHome) {
            $mdDialog.show(
                $mdDialog.alert()
                    .clickOutsideToClose(true)
                    .title(title)
                    .textContent(text)
                    .ariaLabel('Alert Dialog Demo')
                    .ok('Okay')
            );
            if (!!toHome && $location.path() != '/')
                $location.path('/');
        };
        var today = new Date();
        self.minDate = new Date(today.getFullYear(), today.getMonth() - 3, 1);
        self.maxDate = today;
        self.formatDate = util.formatDate;
        self.parseDate = util.parseDate;
        self.parseISODate = util.parseISODate;
        self.filterPastSunday = util.isPastSunday;

        if (userRole === 'admin') {
            function init() {
                $http.get("/metrics").then(function (data) {
                    self.metrics = data.data;
                    self.metrics.totalFree = Math.floor(Number(self.metrics['mem.free']) / 1024);
                    self.metrics.totalMem = Math.floor(Number(self.metrics['mem']) / 1024);
                    self.metrics.serveruptime = util.formatDateTime(new Date(new Date().getTime() - Number(self.metrics.uptime)));
                    self.metrics.users = (self.metrics.counter && self.metrics.counter.status && self.metrics.counter.status['200']) ?
                        self.metrics.counter.status['200'].login : 0;
                }, self.errorHandler);
                query.httpGet("/audit").then(function (r) {
                    self.list = r.data;
                });
            }

            init();
            setInterval(init, 60000);
        }

    });

    module.controller('formController', function ($scope, query) {
        var appCtrl = $scope.appCtrl;
        $scope.domainInfo = appCtrl.domainInfo;
        $scope.results = appCtrl.results;
        $scope.validation = appCtrl.validation;

        $scope.$on("$destroy", function () {
            // clear validation errors when user navigates away.
            $scope.validation = appCtrl.validation = [];
        });

        $scope.handleSubmit = function () {
            query.postJSON("/create", $scope.domainInfo).then(function (data) {
                // $window.location.href = '';
            }, appCtrl.errorHandler);
        };

    });

    module.controller('projectController', function ($scope, query) {
        var appCtrl = $scope.appCtrl;
        $scope.domainInfo = appCtrl.domainInfo;
        $scope.validation = appCtrl.validation;
        $scope.projects = [];
        $scope.project = {};
        $scope.projectId = 0;

        $scope.selectProject = function () {
            if (!$scope.projectId || $scope.projectId == "0") {
                $scope.project = {};
                return true;
            }
            $scope.projects.some(function (el) {
                if (el.id && el.id == $scope.projectId) {
                    $scope.project = el;
                    return true;
                } else
                    return false;
            });
        };

        $scope.saveProject = function () {
            if ($scope.project['id'])
                query.httpPut("/project", $scope.project, true).then(function (data) {
                    appCtrl.showAlert("Success", "Project was successfully updated.", true);
                });
            else
                query.httpPost("/project", $scope.project, true).then(function (data) {
                    appCtrl.showAlert("Success", "Project was successfully created.", true);
            });
        };

        function init() {
            query.httpGet("/project").then(function (response) {
                $scope.projects = response.data;
            });
        }

        init();
    });

    module.controller('profileController', function ($scope, query) {
        var appCtrl = $scope.appCtrl;
        $scope.domainInfo = appCtrl.domainInfo;
        $scope.validation = appCtrl.validation;

        $scope.saveProfile = function () {
            query.httpPost("/profile", $scope.user, true).then(function (data) {
                console.log(data);
            });
        };

        function init() {
            query.httpPost("/user", {
                email: myName
            }, true).then(function (data) {
                $scope.user = data;
            });
        }

        init();
    });

    module.controller('usersController', function ($scope, query) {
        var appCtrl = $scope.appCtrl;
        $scope.domainInfo = appCtrl.domainInfo;
        $scope.validation = appCtrl.validation;
        $scope.view = 'user';
        $scope.project = {};

        $scope.selectProject = function(p) {
            $scope.domainInfo["projectId"]=p.id;
        }

        $scope.updatePerson = function () {
            if ($scope.selectedUser=='default') {
                $scope.domainInfo = {};
                return false;
            }
            query.httpPost("/user", {
                email: $scope.selectedUser
            }).then(function (data) {
                $scope.user = data;
                query.httpPost("/user/project", {email: $scope.user.email}, true).then(function (data) {
                    $scope.project = data[0];
                    if ($scope.project && $scope.project.id > 0)
                        $scope.project['value'] = $scope.project.id;
                    $scope.init();
                });
            });
        }

        $scope.updateUserProfile = function () {
            query.httpPost("/profile/update", $scope.user).then(function (data) {
                if ($scope.project && $scope.project.value > 0) {
                    query.httpPost("/project/adduser", {email: $scope.user.email, projectId: $scope.project.value}, true).then(function (data) {
                        appCtrl.showAlert("Success", "Your profile was successfully updated.", false);
                        $scope.init();
                    });
                } else {
                    appCtrl.showAlert("Success", "Your profile was successfully updated.", false);
                }
            });
        };

        $scope.inviteUser = function () {
            query.httpPost("/create", $scope.domainInfo).then(function (data) {
                appCtrl.showAlert("Success", "User was successfully invited.", false);
                $scope.init();
            });
        }

        $scope.init = function() {
            if (userRole === 'admin') {
                query.httpGet("/users").then(function (response) {
                    $scope.users = response.data;
                });
                query.httpGet("/project/list").then(function (response) {
                    $scope.projects = response.data;
                });
            } else {
                query.httpPost("/user", {
                    email: userName
                }).then(function (data) {
                    $scope.user = data;
                });
            }
        }

        $scope.init();
    });

    module.controller('sheetController', function ($scope, $mdMenu, query, util) {
        var appCtrl = $scope.appCtrl;
        $scope.domainInfo = appCtrl.domainInfo;
        $scope.results = appCtrl.results;
        $scope.validation = appCtrl.validation;
        $scope.days = ['sun', 'mon', 'tue', 'wed', 'thu', 'fri', 'sat'];
        $scope.dates = [];

        $scope.findSheets = function () {
            if (!$scope.selectedUser || $scope.selectedUser=='default') {
                return false;
            }
            $scope.domainInfo['user'] = $scope.selectedUser;
            query.httpPost("/sheet/findsheets", $scope.domainInfo).then(function (data) {
                $scope.sheets = data;
                data.forEach(function(item){
                    $scope.dates.push(new Date(item['beginDt']));
                });
            });
        }

        $scope.saveSheet = function (sheet) {
            query.saveSheet(sheet).then(function(data){
                appCtrl.showAlert("Success", "Time entry was successfully saved.");
            });
        }

        $scope.clearSheet = function (sheet) {
            util.clearSheet(sheet, $scope.days);
        }

        $scope.calcEntry = util.calcEntry($scope.days);

        function init() {
            $mdMenu.hide(null, {closeAll: true});
            if (userRole === 'admin') {
                query.httpGet("/users").then(function (response) {
                        $scope.users = response.data;
                });
            } else {
                query.postJSON("/sheet/mysheets", {
                    "user": userName
                    /**, "status": "submitted" **/
                }).then(function (data) {
                    $scope.sheets = data;
                    $scope.dates = [];
                    if (data.length > 0) $scope.userId = data[0].user.email;
                    $scope.sheets.forEach(function (el) {
                        $scope.dates.push(el.beginDt);
                    });
                }, appCtrl.errorHandler);
            }
        }

        init();
    });

    module.controller('timeController', function ($scope, $mdMenu, query, util) {
        var appCtrl = $scope.appCtrl;
        $scope.domainInfo = appCtrl.domainInfo;
        $scope.results = appCtrl.results;
        $scope.validation = appCtrl.validation;
        $scope.beginDt = new Date();
        $scope.sheet = {};
        $scope.selectedUser = '';
        $scope.userId = "";
        $scope.fileToUpload = [];

        $scope.days = ['sun', 'mon', 'tue', 'wed', 'thu', 'fri', 'sat'];
        $scope.months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
        $scope.dates = [];
        $scope.todayDate = new Date();
        $scope.beginDt = $scope.todayDate;
        $scope.calcEntry = util.calcEntry($scope.days);
        $scope.parseDate = function () {
            if ($scope.sheet && $scope.sheet.beginDt)
                $scope.sheet.beginDt = appCtrl.parseISODate($scope.sheet.beginDt);
        };

        $scope.pickDate = function () {
            query.httpPost("/sheet/addsheet", {
                beginDate: $scope.sheet.beginDt
            }).then(function (data) {
                $scope.sheet = data;
                $scope.parseDate();
            });
        };

        $scope.onFileSelect = function ($files) {
            $scope.fileToUpload = $files;
        };

        $scope.updatePerson = function () {
            $scope.userId = $scope.selectedUser;
            query.httpPost("/sheet/findsheets", {
                "user": $scope.userId
            }).then(function (data) {
                $scope.sheets = data;
            });
        };

        $scope.clearSheet = function (sheet) {
            util.clearSheet(sheet, $scope.days);
        };

        $scope.addSheet = function () {
            query.httpPost("/sheet/addsheet").then(function (data) {
                $scope.sheet = data;
                $scope.parseDate();
            });
        };

        $scope.$on('addSheet', $scope.addSheet);

        $scope.mySheets = function () {
            query.httpPost("/sheet/mysheets", {
                "user": userName
            }).then(function (data) {
                $scope.sheets = data;
                $scope.parseDate();
            });
        };

        $scope.$on('mySheets', $scope.mySheets);

        $scope.findSheets = function () {
            $scope.userId = $scope.selectedUser;
            query.httpPost("/sheet/findsheets", {
                "user": $scope.userId
            }).then(function (data) {
                $scope.sheets = data;
            });
        }

        $scope.saveSheet = function (sheet) {
            if (!sheet.status) sheet.status = 'entered';
            //sheet.begin = appCtrl.formatDate(sheet.begin);
            query.httpPut("/sheet", sheet).then(function (data) {
                appCtrl.showAlert("Success", "Your time entry was successfully entered.", true);
            });
        };

        $scope.submitSheet = function (sheet) {
            sheet.status = 'submitted';
            sheet.begin = appCtrl.formatDate($scope.beginDt);
            if ($scope.fileToUpload.length > 0) {
                query.handleUpload($scope.fileToUpload, sheet).then(function (data) {
                    appCtrl.showAlert("Success", "Your time entry was successfully submitted.", true);
                }, appCtrl.errorHandler);
            } else {
                query.httpPut("/sheet", sheet).then(function (data) {
                    appCtrl.showAlert("Success", "Your time entry was successfully submitted.", true);
                }, appCtrl.errorHandler);
            }
        };

        $scope.deleteSheet = function (sheet) {
            console.log(sheet);
            sheet.begin = appCtrl.formatDate(sheet.begin);
            query.postJSON("/sheet/remove", sheet).then(function (data) {
                var position = -1;
                $scope.sheets.some(function (el, index) {
                    if (el.id === sheet.id) {
                        position = index;
                        return true;
                    }
                    return false;
                })
                if (position > 0) $scope.sheets.splice(position, 1);
            }, appCtrl.errorHandler);
        };

        function init() {
            $mdMenu.hide(null, {closeAll: true});
            if (userRole === 'admin') {
                query.postJSON("/users")
                    .then(function (data) {
                        $scope.users = data;
                    }, appCtrl.errorHandler);
            } else {
                query.httpPost("/sheet/addsheet", {
                    "user": userName
                    /**, "status": "submitted" **/
                }).then(function (data) {
                    $scope.sheet = data;
                    $scope.parseDate();
                });
            }
        }

        init();
    });

    module.controller('resultController', function ($scope, $rootScope, $http, $timeout, $log, $state, $resource, $location, util, query, aggregator, props) {
        var appCtrl = $scope.appCtrl;
        $scope.results = appCtrl.results;
        $scope.isMock = appCtrl.isMock;
        $scope.hasError = appCtrl.hasError;
        $scope.domainInfo = appCtrl.domainInfo;
        $scope.hasSSN = $scope.hasORI = true;
        $scope.filterByAgency = 'DRO';
        $scope.queryCriteria = {};
        $scope.retryCount = -1;
        $scope.results = {};
        $scope.currentDataSource = '';
        $scope.currentQuery = '';
        // reference to timer attached to this scope.
        $scope.retryTimer = 0;

        $scope.$on("$destroy", function () {
            // cancel retry timer.
            $timeout.cancel($scope.retryTimer);
        });

        $scope.filterByAgencyFn = function (system) {
            if (system && system.agency === $scope.filterByAgency)
                return true;
            else
                return false;
        };

        $scope.showSummary = function (system) {
            $scope.currentDataSource = system;
            $scope.currentQuery = (system.match("(.)*_DQ")) ? 'driverQuery' : ((system.match("(.)*_WARRANT"))) ? 'warrantQuery' : 'hotQuery';
            $scope.querySummary = aggregator.getSummary(system);
            // CR-5,777 remove empty ssn and ori column
            $scope.hasSSN = $scope.querySummary.some(function (el) {
                return !!el.ssn
            });
            $scope.hasORI = $scope.querySummary.some(function (el) {
                return !!el.issueORI
            });
        };

        $scope.isVisited = function (adminId) {
            return aggregator.isVisited(adminId)
        };

        // debounce (and reject) requests, if button pressed within props.RETRY_INT timeout interval
        $scope.retryQuery = (function (id, interval) {
            var debounce = util.debounce(id, interval);
            return function () {
                debounce().then(function () {
                    var qyRetry = {
                        queryId: aggregator.getQueryId()
                    };
                    ng.extend(qyRetry, $scope.domainInfo);
                    query.retryQuery(qyRetry, {
                        "retry": $scope.retryCount
                    }).then(function (data) {
                        data = (data.status) ? data.data : data;
                        aggregator.ingest(data);
                        init();
                    }, appCtrl.errorHandler);
                }, function () {
                    $log.log("canceled an http request of type, " + id);
                });
            };
        })('retry', props.RETRY_INT);

        function init() {
            $scope.results = aggregator.getData();
            // if no data in cache then redirect to /root
            if (!$scope.results['rsQueryId']) {
                $location.path('/');
                return false;
            }
            // initialize with current query criteria.
            $scope.queryCriteria = aggregator.getCriteria();
            if ($scope.retryCount < props.RETRIES) {
                $scope.retryTimer = $timeout($scope.retryQuery, props.RETRY_INT);
                $scope.retryCount++;
            }
        }

        init();

        /**

         $resource('/mocks/summary.json').get(function(data) {
            aggregator.ingest(data);
            $scope.results = aggregator.getData();
        });
         **/

    });

});
