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
})