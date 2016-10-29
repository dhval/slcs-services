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
    })