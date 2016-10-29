/**
### http://stackoverflow.com/questions/26321405/angularjs-how-do-i-update-dom-changes-made-with-outside-code-i-have-no-control
**/
angular.module('directives').directive("autofill", function() {
    return {
        require: "ngModel",
        link: function(scope, element, attrs, ngModel) {
            scope.$on("autofill-update", function() {
                ngModel.$setViewValue(element.val());
            });
        }
    };
})