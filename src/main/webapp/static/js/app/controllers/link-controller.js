(function () {

    var dhd = angular.module('dhd');

    dhd.controller('LinkController', ['$http', '$scope', function ($http, $scope) {

        var self = this;
        self.querySearch = querySearch;
        self.linkDoc = linkDoc;

        function linkDoc() {
            console.log(self.docId + " " + self.selectedItem);
        };

        function querySearch(query) {
            return [{"documentName":"abc","id":"1"},{"documentName":"qwe","id":"2"}];
            //return $http({url: '/document/autocomplete/' + query, method: 'get'}).then(function (succ) {
              //  return succ.data;
            //});
        };
    }]);

})();