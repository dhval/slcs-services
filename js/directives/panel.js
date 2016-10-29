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
})