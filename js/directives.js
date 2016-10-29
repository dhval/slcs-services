angular.module('directives',[])
;
angular.module('directives').directive("bingUrl", function() {
    return {
        restrict: "A",
        scope: {
            bingUrl: '@'
        },
        link: function(scope, element, attrs) {
            var bingUrl = "http://www.bing.com/maps/default.aspx?q=" + scope.bingUrl;
            element.bind('click', function() {
                if (confirm('You are about to leave JNET secure page. Do you want to continue?')) {
                    window.open(bingUrl, 'windowname1');
                    return true;
                } else {
                    return false;
                }
            });
        }
    }
});
angular.module('directives').directive("autofill", function() {
    return {
        require: "ngModel",
        link: function(scope, element, attrs, ngModel) {
            scope.$on("autofill-update", function() {
                ngModel.$setViewValue(element.val());
            });
        }
    };
});
angular.module('directives').directive('displayDetails',
    function($q, $log, $compile, $templateCache, aggregator, util) {
        function showDetails(errorHandler) {
            return function(scope, tableId) {
                function applyTemplate(data) {
                    data = (data.status) ? data.data : data;
                    var template = util.getTemplate(data['type']);
                    var index = scope.$index;
                    if (data.data) {
                        scope.data = data.data;
                        scope.queryTime = data.queryTime;
                    } else {
                        scope.data = data;
                    }
                    var node = document.getElementById(data.id);
                    if (node) {
                        // remove node
                        node.parentNode.removeChild(node);
                        $('tr#t_' + index, tableId).removeClass('available');
                        scope.showPDF = false;
                        return false;
                    }
                    // debugger;
                    var contents = $compile(template)(scope);
                    $el = $('tr#t_' + index, tableId);
                    $el.addClass('available');
                    scope.showPDF = true;
                    var $contents = $(contents).attr('id', data.id).addClass('row-details');
                    $el.after($contents);
                }
                var promise = aggregator.getDetails(scope.system);
                if (promise.then)
                    promise.then(applyTemplate, errorHandler);
                else applyTemplate(promise);
            }
        }

        return {
            restrict: "A",
            link: function($scope, $element, $attrs) {
                $scope.showDetails = showDetails($scope.appCtrl.errorHandler);
            }
        }
    });
angular.module('directives').filter('formatPattern', function() {
    return function(input, type) {
        if (!input || !type) return input;
        switch (type) {
            case "SSN":
            case "ssn":
            case "fieldSSN":
                if (input.length == 9)
                    return input.slice(0, 3) + "-" + input.slice(3, 5) + "-" + input.slice(5);
            case "SID":
            case "sid":
            case "fieldSID":
                if (input.length == 8)
                    return input.slice(0, 3) + "-" + input.slice(3, 5) + "-" + input.slice(5, 7) + "-" + input.slice(7);
            case "SEX":
            case "sex":
                if (input === 'M') return 'Male';
                else if (input === 'F') return 'Female';
                else if (input === 'U') return 'Unknown';
                else return input;
            case "removeUS":
                return input.replace(/_DQ|_/g, " ");
        }
        return input;
    };
});
angular.module('directives').directive('inputMask', function($log) {
    return {
        restrict: 'A',
        require: "ngModel",
        link: function(scope, element, attrs, ngModel) {
            $(element).inputmask(attrs.inputMask);

            //format text going to user (model to view)
            ngModel.$formatters.push(function(value) {
                // debugger;
                return value;
            });

            //format text from the user (view to model)
            ngModel.$parsers.push(function(value) {
                // debugger;
                if (value instanceof Date) {
                    var yyyy = value.getFullYear().toString();
                    var mm = (value.getMonth() + 1).toString(); // getMonth() is zero-based
                    var dd = value.getDate().toString();
                    var stDate = (mm[1] ? mm : "0" + mm[0]) + "/" +
                        (dd[1] ? dd : "0" + dd[0]) + "/" + yyyy; // padding
                    return stDate;
                }
                return value;
            });
        }
    }
});
angular.module('directives').directive('panel', function($log) {
    return {
        restrict: "A",
        link: function($scope, $element, $attrs) {
            // debugger;
            $("#footer-expanded", "div#footer").on('shown.bs.collapse', function(i) {
                $("#stateDisclaimer", $element).removeClass("glyphicon-plus").addClass("glyphicon-minus");
            });
            $("#footer-expanded", "div#footer").on('hidden.bs.collapse', function(i) {
                $("#stateDisclaimer", $element).removeClass("glyphicon-minus").addClass("glyphicon-plus");
            });
        }
    };
});
angular.module('directives').directive("autotab", function() {
    var delay = (function() {
        var timer = 0;
        return function(callback, ms, parameter) {
            clearTimeout(timer);
            timer = setTimeout(function() {
                callback(parameter)
            }, ms);
        };
    })();
    var nodes = [];
    /* TAB, SHIFT, CTRL(9, 16, 17) Return false */
    function isKeyCode(keyCode) {
        if (keyCode >= 47 && keyCode <= 90) {
            return true;
        } else if (keyCode >= 96 && keyCode <= 110) {
            return true;
        } else if (keyCode >= 186 && keyCode <= 222) {
            return true;
        } else
            return false;
    }

    function focus(attrs) {
        // all node having 'name' attribute, are potential candidate for HTML form.
        var node = null;
        if (nodes.length === 0)
            nodes = document.querySelectorAll("input[name], select[name]");
        Array.prototype.slice.call(nodes, 0).forEach(function(el, index) {
            if (el.getAttribute('name') === attrs['name'] && (index + 1 < nodes.length)) {
                node = nodes[index + 1];
                node.focus();
                node.select();
                return node;
            }
        });

    }
    $.fn.focusNextInputField = function() {
        return this.each(function() {
            var fields = $(this).parents('form:eq(0),body').find(
                'select,textarea,input');
            var index = fields.index(this);
            if (index > -1 && (index + 1) < fields.length) {
                fields.eq(index + 1).select();
                fields.eq(index + 1).focus();
            }
            return false;
        });
    };
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            element.bind('keyup', function(e) {
                // debugger;
                var keyCode = e.keyCode || e.which;
                if (keyCode === 13) {
                    $(this).closest("form").submit();
                } else if (isKeyCode(keyCode)) {
                    delay(function(obj) {
                        var newValue = $(obj).val();
                        // see if the number of entered characters is equal or greater
                        // than the allowable maxlength
                        if (newValue.match(/[^_]*/)[0].length >= attrs['maxlength']) {
                            // set focus on the next field with autotab class
                            //$(obj).focusNextInputField();
                            focus(attrs);
                        }
                    }, 100, this);
                }
            });
        }
    };
});
angular.module('directives').directive('ngFiles', ['$parse', function ($parse) {
    function fn_link(scope, element, attrs) {
        var onChange = $parse(attrs.ngFiles);
        element.on('change', function (event) {
            onChange(scope, {$files: event.target.files});
        });
    };
    return {
        link: fn_link
    }
}]);