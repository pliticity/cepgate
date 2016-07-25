(function () {

    var common = angular.module('common');

    common.directive("cgCloseTab", ['$compile',function ($compile) {

        return {
            restrict: 'A',
            scope:{
                cgCloseTab: '=cgCloseTab'
            },
            link: function link(scope, element, attrs, controller, transcludeFn) {
                var tabsId = scope.cgCloseTab.tabsId;
                var tabId = "{0}-{1}".format(tabsId,scope.cgCloseTab.id);

                var x = "<span style='z-index:100;margin-left: 5px;' ng-click=\"closeTab('{0}','{1}')\" class='glyphicon glyphicon-remove-circle' aria-hidden='true'></span>".format(tabId,tabsId);
                element.append($compile(x)(scope));

                scope.closeTab = function (tab,tabsId) {
                    $("#tab-"+tab).remove();
                    $("#"+tab).remove();

                    $('#'+tabsId+' a:last').tab('show');
                };
            }
        };

    }]);

})();