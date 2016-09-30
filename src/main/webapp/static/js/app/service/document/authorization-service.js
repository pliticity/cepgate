(function () {

    var document = angular.module('document');

    document.service('authorizationService', ['$http', '$window', function ($http) {

        var service = this;

        service.principal = {};

        service.ready = false;

        service.getPrincipal = function () {
            $http({url: '/principal/current', method: 'get'}).then(function (succ) {
                service.principal = succ.data;
                service.ready = true;
            });
        };

        service.hasAtLeastOneRole = function(roles){
            while(service.ready && service.ready == false){

            }
            if(service.principal.role=='ADMIN' || service.principal.role=='GLOBAL_ADMIN'){
                return true;
            }else{
                var flag = false;
                roles.forEach(function(i,e){
                    if(i == service.principal.role){
                        flag = true;
                    }
                });
                return flag;
            }
        };

        this.getPrincipal();

    }]);

})();