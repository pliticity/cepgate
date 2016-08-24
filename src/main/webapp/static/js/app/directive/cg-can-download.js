(function () {

    var common = angular.module('common');

    common.directive("canDownload", ['$compile',function ($compile) {

        var cgValidate = this;
        cgValidate.errorClass = 'has-error';

        return {
            restrict: 'A',
            scope : {
                canDownload : '=canDownload'
            },
            link : function link(scope, element, attrs, controller, transcludeFn){
                scope.$watch(function(){
                    var flag = $("table#" + scope.canDownload + " tr.st-selected td[data='files'] select option:selected").length > 0;
                    if (flag) {
                        attrs.$set('ng-disabled', false);
                    } else {
                        attrs.$set('ng-disabled', true);
                    }
             });
            }
        }


    }]);

})();