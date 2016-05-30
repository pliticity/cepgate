(function () {

    var dhd = angular.module('dhd');

    dhd.directive("refreshTab", ['$compile', function ($compile) {
        return function (scope, element, attrs) {
            element.bind("click", function () {
                var id = attrs.ariaControls;
                var content = $("#"+id);
                content.html($compile(content.html())(scope));
            });
        };
    }]);

})();