(function () {

    var common = angular.module('common');

    common.directive("refreshTab", ['$compile', function ($compile) {
        return function (scope, element, attrs) {
            element.bind("click", function () {
                var id = attrs.ariaControls;
                var content = $("#"+id);
                content.html($compile(content.html())(scope));
            });
        };
    }]);

})();