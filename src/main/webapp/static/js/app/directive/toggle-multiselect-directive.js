(function () {

    var common = angular.module('common');

    common.directive("toggleMultiSelect", ['$compile', '$timeout', function ($compile, $timeout) {
        return function (scope, element, attributes) {
            $timeout(function () {
                element.ready(function () {
                    element = $(element[0]);
                    if (element.parent(".hide-native-select").length < 1) {
                        var func = function(){};
                        if(attributes.toggleMultiSelect=='apply'){
                            func = function (option, checked, select) {
                                scope.$apply();
                            };
                        }
                        element.multiselect({
                            includeSelectAllOption: false,
                            numberDisplayed: 1,
                            onChange: func,
                            onSelectAll: func
                        });
                    }
                });
            });
        }
    }]);

})();