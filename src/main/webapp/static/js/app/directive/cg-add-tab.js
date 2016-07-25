(function () {

    var common = angular.module('common');

    common.directive("cgAddTab", ['tabService', function (tabService) {

        var cgAddTab = this;

        cgAddTab.bind = function(el,args,scope){
            el.bind("click", function () {
                tabService.addTab(args,scope);
            });
        };

        return {
            restrict: 'A',
            scope: {
                cgAddTab: '=cgAddTab'
            },
            link: function link(scope, element, attrs, controller, transcludeFn) {
                if(scope.cgAddTab.onChildren == true){
                    var children = element.children();
                    if(children.length > 0){
                        for(var i = 0; i < children.length; i++){
                            if(scope.cgAddTab.except == null || (scope.cgAddTab.except != null && !scope.cgAddTab.except.containsObject(i))){
                                var el = angular.element(children[i]);
                                cgAddTab.bind(el,scope.cgAddTab,scope);
                            }
                        }
                    }
                }else{
                    cgAddTab.bind(element,scope.cgAddTab,scope);
                }
            }
        };
    }]);

})();