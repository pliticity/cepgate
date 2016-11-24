(function () {

    var common = angular.module('common');

    common.controller('LinkController', ['$http', '$scope','$window','linkService', function ($http, $scope,$window,linkService) {

        var self = this;
        self.querySearch = querySearch;
        self.link = link;
        self.unlink = unlink;
        self.links = [];
        self.withRevision = false;
        self.classification = true;

        self.getLinks = function (model, oType, linkOType) {
            model.$promise.then(function () {
                var oId = model.id;
                if (oId != null && oType != null) {
                    self.model = {oId: oId, oType: oType};
                    $http({
                        url: '/link',
                        method: 'get',
                        params: {oId: oId, oType: oType, linkOType: linkOType}
                    }).then(function (response) {
                        if (response.data.length > 0) {
                            for (var i = 0; i < response.data.length; i++) {
                                var bond = response.data[i];
                                var number = 'second';
                                if (bond.firstId != oId) {
                                    number = 'first';
                                }
                                self.fetchObject(bond, number);
                            }
                        }
                    });
                }
            });
        };

        self.fetchObject = function(bond,number){
            $http({url:'/link/'+bond.id,method:'get',params:{number:number,classification:self.classification}}).then(function(response){
                bond.object = response.data;
                self.links.push(bond);
            });
        };

        function link(oType) {
            var newBond = {
                firstId : self.model.oId,
                firstType : self.model.oType,
                firstRevision : false,
                secondId : self.selectedItem.id,
                secondType : oType,
                secondRevision : self.withRevision
            };
            $http({url: '/link', method: 'post',data:newBond}).then(function (response) {
                self.fetchObject(response.data,'second');
            });
        };

        function unlink(link) {
            $http({url: '/unlink', method: 'post',params:{linkId:link.id}}).then(function (succ) {
                var index = null;
                for(var i=0; i<self.links.length; i++){
                    if(self.links[i].id == link.id){
                        index = i;
                    }
                }
                if(index!=null){
                    self.links.splice(index);
                };
            });
        };

        self.openDic = function(id){
            $window.location.href = '/document#/dic/'+id;
        };

        self.openPic = function(id){
            $window.location.href = '/product#/pic/'+id;
        };

        function querySearch(query, oType) {
            $("md-virtual-repeat-container").each(function (i, e) {
                $(e).css("z-index", "2000")
            });
            return $http({url: '/'+oType+'/autocomplete/' + query, method: 'get'}).then(function (succ) {
                return succ.data;
            });
        };
    }]);

})();