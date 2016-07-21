(function () {

    var common = angular.module('common');

    common.directive("cgValidate", [function () {

        var cgValidate = this;
        cgValidate.errorClass = 'has-error';

        return {
            restrict: 'A',
            scope : {
                cgValidate : '=cgValidate'
            },
            link : function link(scope, element, attrs, controller, transcludeFn){
                var form = scope.cgValidate.form;
                var field = form[scope.cgValidate.field];
             scope.$watch(function(){
                 if(form.$submitted && field.$invalid){
                     element.addClass(cgValidate.errorClass);
                 }else{
                     element.removeClass(cgValidate.errorClass);
                 }
             });
            }
        }


    }]);

})();