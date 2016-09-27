(function () {

    var common = angular.module('common');

    common.controller('LinkController', ['$http', '$scope','$window','linkService', function ($http, $scope,$window,linkService) {

        var self = this;
        self.querySearch = querySearch;
        self.link = link;
        self.unlink = unlink;
        self.setLinkable = setLinkable;
        self.linkable = {};
        self.linkableType = 'document';

        function link() {
            $http({url: '/link', method: 'post',params:{parentId:self.linkable.id,parentType:self.linkableType,objectId:self.selectedItem.id,objectType: 'document',linkType:'LINK'}}).then(function (succ) {
                for(var i=0; i<succ.data.length; i++){
                    linkService.getObject(succ.data[i]);
                }
                self.linkable.links = succ.data;
                self.selectedItem = null;
            });
        };

        function unlink(link) {
            $http({url: '/unlink', method: 'post',params:{parentId:self.linkable.id,parentType:self.linkableType}, data: link}).then(function (succ) {
                self.linkable.links = succ.data;
            });
        };

        function setLinkable(linkable, type) {
            self.linkable = linkable;
            self.linkableType = type;
        };

        self.openDic = function(id){
            $window.location.href = '/document#/dic/'+id;
        };

        function querySearch(query) {
            $("md-virtual-repeat-container").each(function (i, e) {
                $(e).css("z-index", "2000")
            });
            return $http({url: '/document/autocomplete/' + query, method: 'get'}).then(function (succ) {
                return succ.data;
            });
        };
    }]);

})();