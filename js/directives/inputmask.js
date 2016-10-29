/**
 **  https://github.com/RobinHerbots/jquery.inputmask
 **/
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
})