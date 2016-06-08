(function () {

    var dhd = angular.module('dhd');

    dhd.directive("toggleMultiSelect", ['$compile', '$timeout', function ($compile, $timeout) {
        return function (scope, element, attributes) {
            $timeout(function () {
                element.ready(function () {
                    element = $(element[0]);
                    element.multiselect({includeSelectAllOption: true, numberDisplayed: 1});
                });
            });
        }
    }]);

})();