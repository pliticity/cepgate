(function () {

    var dhd = angular.module('dhd');

    dhd.controller('LinkController', ['$http', '$scope', function ($http, $scope) {

        var self = this;
        self.querySearch = querySearch;
        self.linkDoc = linkDoc;

        function linkDoc(docId) {
            $http({url: '/document/link/' + docId, method: 'post', data: self.selectedItem}).then(function (succ) {
                $scope.documentInfo.links = succ.data;
                self.selectedItem = null;
            });
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