(function () {

    var dhd = angular.module('dhd');

    dhd.directive("closeTab", ['$compile', function ($compile) {
        return function (scope, element, attrs) {

            console.log(attrs);
            var id = attrs.closeTab;

            element.append("<a href=\"#\" ng-click=\"closeTab('"+id+"')\"><span class=\"glyphicon glyphicon-remove-circle\" aria-hidden=\"true\"></span></a>");

            $scope.closeTab = function (tab) {
                console.log("closing "+tab);    
            };
        };
    }]);

})();