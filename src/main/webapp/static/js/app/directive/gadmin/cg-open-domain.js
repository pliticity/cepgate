(function () {

    var common = angular.module('common');

    common.directive("cgOpenDomain", ['$window', function ($window) {

        var cgOpenDomain = this;

        cgOpenDomain.bind = function(el,id){
            el.bind("click", function () {
                $window.open("/admin#/" + id, '_self');
            });
        };

        return {
            restrict: 'A',
            scope: {
                cgOpenDomain: '=cgOpenDomain'
            },
            link: function link(scope, element, attrs, controller, transcludeFn) {
                    var children = element.children();
                    if(children.length > 0){
                        for(var i = 0; i < children.length; i++){
                            if(i!=4){
                                var el = angular.element(children[i]);
                                cgOpenDomain.bind(el,scope.cgOpenDomain);
                            }
                        }
                    }
            }
        };
    }]);

})();