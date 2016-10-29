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
})