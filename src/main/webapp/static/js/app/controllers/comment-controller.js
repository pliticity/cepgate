(function () {

    var document = angular.module('document');

    document.controller('CommentController', ['$http', '$scope', function ($http, $scope) {

        var ctrl = this;

        ctrl.commentForm = {};
        ctrl.comment = {};
        ctrl.dic = {};

        ctrl.addComment = function() {
            if (ctrl.commentForm.$valid) {
                $http({
                    url: '/document/' + ctrl.dic.id + '/comment',
                    method: 'post',
                    data: ctrl.comment
                }).then(function (succ) {
                    ctrl.dic.comments = succ.data;
                    ctrl.newComment();
                });
            }
        };

        ctrl.newComment = function() {
            $http({url: '/document/' + ctrl.dic.id + '/comment', method: 'get'}).then(function (succ) {
                ctrl.comment = succ.data;
            });
        }

    }]);

})();