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
})