(function () {

    var common = angular.module('common');

    common.directive("closeTab", ['$compile', function ($compile) {
        return function (scope, element, attrs) {
            var id = attrs.closeTab;

            var x = "<span style=\"z-index:100;margin-left: 5px;\" ng-click=\"closeTab('"+id+"')\" class=\"glyphicon glyphicon-remove-circle\" aria-hidden=\"true\"></span>";

            element.append($compile(x)(scope));

            scope.closeTab = function (tab) {
                $("#tab-"+tab).remove();
                $("#"+tab).remove();

                $('#documentTabs a:last').tab('show');
            };
        };
    }]);

})();