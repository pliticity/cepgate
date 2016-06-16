(function () {

    var dhd = angular.module('dhd');

    dhd.controller('CommentController', ['$http', '$scope', function ($http, $scope) {

        var self = this;
        $scope.form = {};
        $scope.comment = {};

        $scope.addComment = addComment;

        function addComment() {
            if ($scope.form.addCommentForm.$valid) {
                $http({
                    url: '/document/' + $scope.documentInfo.id + '/comment',
                    method: 'post',
                    data: $scope.comment
                }).then(function (succ) {
                    $scope.documentInfo.comments = succ.data;
                    newComment();
                });
            }
        };

        function newComment() {
            $http({url: '/document/' + $scope.documentInfo.id + '/comment', method: 'get'}).then(function (succ) {
                $scope.comment = succ.data;
            });
        }

        newComment();


    }]);

})();