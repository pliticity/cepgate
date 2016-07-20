    (function () {

    var common = angular.module('common');

        common.directive('hasRole',['authorizationService','$compile', function (authorizationService,$compile) {
        return function (scope, element, attrs) {
            //element.removeAttr('has-role');
            var roles = [];
            attrs.hasRole.split(',').forEach(function(i,e){
                roles.push(i);
            });
            var hasRole = authorizationService.hasAtLeastOneRole(roles);
            //var bool = hasRole+"==true";
            if(hasRole!=true){
                element.remove();
            }
/*            console.log(attrs);
            if(attrs.ngIf){
                attrs.$set('ng-if', attrs.ngIf+" && "+bool);
            }else{
                element.attr('ng-if',   bool);
            }
            $compile(element)(scope);*/
        };
    }]);

})();