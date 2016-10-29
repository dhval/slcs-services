angular.module('directives').directive("aopcLink", function() {
    return {
        restrict: "A",
        scope: {
            aopcLink: '@',
            matterKey: '@',
            docketNum: '@'
        },
        link: function(scope, element, attrs) {
            var bingUrl = "https://www.aopc.test.jnet.state.pa.us/login.aspx?ReturnURL=";
            var isMDJ = aopcLink.startsWith("MDJ");
            if(scope.matterKey) {
                bingUrl += "aopc.url.param.matterid=/Secure/DocketSheets/CourtSummaryReport.aspx%3FmatterID=" + scope.matterKey.replace("DIS", "");
            } else if(scope.docketNum) {
                if (isMDJ)
                    bingUrl += "/Secure/DocketSheets/MDJReport.aspx%3Fdistrict=" + scope.aopcLink + "&docketNumber=" + scope.docketNum;
                else
                    bingUrl += "/Secure/DocketSheets/CPReport.ashx?docketNumber=" + scope.docketNum;
            }
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