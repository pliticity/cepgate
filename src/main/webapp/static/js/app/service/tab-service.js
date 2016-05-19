(function () {

    var dhd = angular.module('dhd');

    dhd.service('TabService', function ($compile) {

        this.addTab = function (name,id) {
            var link = "<li role='presentation'><a href='#"+id+"' aria-controls='"+id+"' role='tab'data-toggle='tab'>"+name+"</a></li>";
            var tab = "<div role='tabpanel' class='tab-pane' id='"+id+"' ng-controller='DocumentController'> <div ng-init=\"get('"+id+"')\"></div> <ng-include src=\"'/partials/document/details.html'\"></ng-include></div>";

            $("#documentTabs").append($compile(link));
            $("#documentsTabContent").append($compile(tab));

        };

    });

})();
