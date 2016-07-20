(function () {

    var common = angular.module('common');

    common.directive("addTab", ['$compile', function ($compile) {
        return function (scope, element, attrs) {
            element.bind("click", function () {

                var id = attrs.documentid;
                var name = attrs.documntname;

                var e = $("#tab-"+id);
                if(e.length>0){
                    $("#tab-"+id+" a:last").tab('show');
                    return;
                }

                var link = "<li id='tab-"+id+"' role='presentation'><a close-tab="+id+" href='#" + id + "' aria-controls='" + id + "' role='tab'data-toggle='tab'><span id='tab-name-"+id+"'>" + name + "</span></li>";
                var tab = "<div role='tabpanel' class='tab-pane' id='" + id + "' ng-controller='DocumentController'> <div ng-init=\"get('" + id + "')\"></div> <ng-include src=\"'/partials/document/details.html'\"></ng-include></div>";

                $("#documentTabs").append($compile(link)(scope));
                $("#documentsTabContent").append($compile(tab)(scope));

                $("#tab-"+id+" a:last").tab('show');

            });
        };
    }]);

})();