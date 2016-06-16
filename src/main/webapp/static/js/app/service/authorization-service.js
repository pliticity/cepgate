(function () {

    var dhd = angular.module('dhd');

    dhd.service('authorizationService', ['$http', '$window', function ($http) {

        this.principal = {};

        this.ready = false;

        this.getPrincipal = function () {
            $http({url: '/principal/current', method: 'get'}).then(function (succ) {
                principal = succ.data;
                ready = true;
            });
        };

        this.hasAtLeastOneRole = function(roles){
            while(ready && ready == false){

            }
            if(principal.role=='ADMIN' || principal.role=='GLOBAL_ADMIN'){
                return true;
            }else{
                var flag = false;
                roles.forEach(function(i,e){
                    if(i == principal.role){
                        flag = true;
                    }
                });
                return flag;
            }
        };

        this.getPrincipal();

    }]);

})();