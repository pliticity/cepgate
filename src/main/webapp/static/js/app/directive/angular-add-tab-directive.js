(function () {

    var dhd = angular.module('dhd');

    dhd.directive("addTab", ['$compile', function ($compile) {
        return function (scope, element, attrs) {
            element.bind("click", function () {
                var id = attrs.documentid;
                var name = attrs.documntname;

                var link = "<li role='presentation'><a href='#" + id + "' aria-controls='" + id + "' role='tab'data-toggle='tab'>" + name + "</a></li>";
                var tab = "<div role='tabpanel' class='tab-pane' id='" + id + "' ng-controller='DocumentController'> <div ng-init=\"get('" + id + "')\"></div> <ng-include src=\"'/partials/document/details.html'\"></ng-include></div>";

                $("#documentTabs").append($compile(link)(scope));
                $("#documentsTabContent").append($compile(tab)(scope));

            });
        };
    }]);

})();