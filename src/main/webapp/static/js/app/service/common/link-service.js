(function () {

    var common = angular.module('common');

    common.service('linkService', ['$http', function ($http) {

        var service = this;

        this.fetchObjects = function(response){
            var links = response.data.links;
            if(links!=null){
                for(var i=0; i<links.length; i++){
                    service.getObject(response.resource.links[i]);
                }
            }
        }

        this.getObject = function(link) {
            $http({url: '/link/'+link.id, method: 'get'}).then(function (succ) {
                link.object = succ.data;
            });
        };

    }]);

})();