    (function () {

    var dhd = angular.module('dhd');

    dhd.directive('hasRole',['authorizationService','$compile', function (authorizationService,$compile) {
        return function (scope, element, attrs) {
            element.removeAttr('has-role');
            var roles = [];
            attrs.hasRole.split(',').forEach(function(i,e){
                roles.push(i);
            });
            var hasRole = authorizationService.hasAtLeastOneRole(roles);
            var bool = "'"+hasRole+"'=='true'";
            console.log(attrs);
            if(attrs.ngIf){
                attrs.$set('ng-if', attrs.ngIf+" && "+bool);
            }else{
                element.attr('ng-if',   bool);
            }
            $compile(element)(scope);
        };
    }]);

})();